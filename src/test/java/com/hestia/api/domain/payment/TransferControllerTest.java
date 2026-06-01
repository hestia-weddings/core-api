package com.hestia.api.domain.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hestia.api.domain.payment.entity.Transaction;
import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.enums.TransactionStatus;
import com.hestia.api.domain.payment.repository.TransactionRepository;
import com.hestia.api.domain.payment.repository.WalletRepository;
import com.hestia.api.infrastructure.asaas.AsaasTransferClient;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferResponse;
import com.hestia.api.security.support.WithMockCouple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Transfer Controller")
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @MockitoBean
    private AsaasTransferClient asaasTransferClient;

    private static final String WALLET_ID = "11111111-1111-1111-1111-111111111111";
    private static final String WEBHOOK_TOKEN = "wh_test_token_abc123def456ghi789jkl012mno345pqr678st";

    @Test
    @WithMockCouple
    void createsTransferSuccessfully() throws Exception {
        when(asaasTransferClient.createTransfer(any()))
                .thenReturn(new AsaasTransferResponse("asaas-transfer-001", "PENDING", BigDecimal.valueOf(100)));

        mockMvc.perform(
                        post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"amount": 10000}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(10000))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockCouple
    void returnsUnprocessableWhenInsufficientBalance() throws Exception {
        mockMvc.perform(
                        post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"amount": 99999}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockCouple
    void transferDoneWebhookCompletesTransaction() throws Exception {
        when(asaasTransferClient.createTransfer(any()))
                .thenReturn(new AsaasTransferResponse("asaas-done-001", "PENDING", BigDecimal.valueOf(100)));

        // Create a transfer first
        mockMvc.perform(
                        post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"amount": 10000}
                                """))
                .andExpect(status().isOk());

        Wallet walletBefore =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        int balanceBefore = walletBefore.getBalance();

        // Send TRANSFER_DONE webhook
        mockMvc.perform(
                        post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"id": "evt_1", "event": "TRANSFER_DONE", "transfer": {"id": "asaas-done-001"}}
                                """))
                .andExpect(status().isOk());

        Transaction transaction =
                transactionRepository.findByAsaasTransferId("asaas-done-001").orElseThrow();
        assert transaction.getStatus() == TransactionStatus.COMPLETED;

        Wallet walletAfter =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assert walletAfter.getBalance() == balanceBefore - transaction.getAmount() - transaction.getFee();
    }

    @Test
    @WithMockCouple
    void transferDoneWebhookIsIdempotent() throws Exception {
        when(asaasTransferClient.createTransfer(any()))
                .thenReturn(new AsaasTransferResponse("asaas-idem-001", "PENDING", BigDecimal.valueOf(100)));

        mockMvc.perform(
                        post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"amount": 10000}
                                """))
                .andExpect(status().isOk());

        // First webhook
        mockMvc.perform(
                        post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"id": "evt_2", "event": "TRANSFER_DONE", "transfer": {"id": "asaas-idem-001"}}
                                """))
                .andExpect(status().isOk());

        Wallet walletAfterFirst =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        int balanceAfterFirst = walletAfterFirst.getBalance();

        // Duplicate webhook — balance should not change
        mockMvc.perform(
                        post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"id": "evt_3", "event": "TRANSFER_DONE", "transfer": {"id": "asaas-idem-001"}}
                                """))
                .andExpect(status().isOk());

        Wallet walletAfterSecond =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assert walletAfterSecond.getBalance() == balanceAfterFirst;
    }

    @Test
    @WithMockCouple
    void transferFailedWebhookMarksTransactionFailed() throws Exception {
        when(asaasTransferClient.createTransfer(any()))
                .thenReturn(new AsaasTransferResponse("asaas-fail-001", "PENDING", BigDecimal.valueOf(100)));

        mockMvc.perform(
                        post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"amount": 10000}
                                """))
                .andExpect(status().isOk());

        Wallet walletBefore =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        int balanceBefore = walletBefore.getBalance();

        // Send TRANSFER_FAILED webhook
        mockMvc.perform(
                        post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"id": "evt_4", "event": "TRANSFER_FAILED", "transfer": {"id": "asaas-fail-001"}}
                                """))
                .andExpect(status().isOk());

        Transaction transaction =
                transactionRepository.findByAsaasTransferId("asaas-fail-001").orElseThrow();
        assert transaction.getStatus() == TransactionStatus.FAILED;

        Wallet walletAfter =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assert walletAfter.getBalance() == balanceBefore;
    }
}
