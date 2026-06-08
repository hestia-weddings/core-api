package com.hestia.api.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.repository.TransactionRepository;
import com.hestia.api.domain.payment.repository.WalletRepository;
import com.hestia.api.domain.registry.entity.Order;
import com.hestia.api.domain.registry.enums.OrderStatus;
import com.hestia.api.domain.registry.repository.OrderRepository;
import com.hestia.api.infrastructure.asaas.AsaasCheckoutClient;
import com.hestia.api.infrastructure.asaas.AsaasTransferClient;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutResponse;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferResponse;
import com.hestia.api.security.support.WithMockCouple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "asaas.webhook-token=wh_test_token_abc123def456ghi789jkl012mno345pqr678st")
@Transactional
@DisplayName("Payment Lifecycle E2E")
class PaymentLifecycleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockitoBean
    private AsaasCheckoutClient asaasCheckoutClient;

    @MockitoBean
    private AsaasTransferClient asaasTransferClient;

    private static final String SLUG = "alice-bob";
    private static final String GIFT_ID = "eeee0000-0000-0000-0000-000000000001";
    private static final String WALLET_ID = "11111111-1111-1111-1111-111111111111";
    private static final String WEBHOOK_TOKEN = "wh_test_token_abc123def456ghi789jkl012mno345pqr678st";
    private static final String ASAAS_CHECKOUT_ID = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    private static final String ASAAS_TRANSFER_ID = "transfer-001";

    @BeforeEach
    void setUp() {
        AsaasCheckoutResponse checkoutResponse = new AsaasCheckoutResponse();
        checkoutResponse.setId(ASAAS_CHECKOUT_ID);
        checkoutResponse.setLink("https://sandbox.asaas.com/checkout/" + ASAAS_CHECKOUT_ID);
        checkoutResponse.setStatus("ACTIVE");
        when(asaasCheckoutClient.createCheckout(any())).thenReturn(checkoutResponse);

        when(asaasTransferClient.createTransfer(any()))
                .thenReturn(new AsaasTransferResponse(ASAAS_TRANSFER_ID, "PENDING", BigDecimal.valueOf(100)));
    }

    @Test
    @DisplayName("Full lifecycle: guest pays → webhook credits wallet → couple withdraws → webhook completes")
    @WithMockCouple
    void fullPaymentLifecycle() throws Exception {
        Wallet walletBefore =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        int initialBalance = walletBefore.getBalance();

        // === STEP 1: Guest creates checkout ===
        mockMvc.perform(post("/w/{slug}/gift/{giftId}/checkout", SLUG, GIFT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"guest_name\": \"Maria\", \"guest_email\": \"maria@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkout_url").exists());

        // Verify order was created as PENDING
        Order order = orderRepository.findAll().stream()
                .filter(o -> o.getGuestName().equals("Maria"))
                .findFirst()
                .orElseThrow();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        int orderAmount = order.getAmount();

        // === STEP 2: Asaas sends CHECKOUT_PAID webhook ===
        mockMvc.perform(post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {"id": "evt_1", "event": "CHECKOUT_PAID", "checkout": {"id": "%s", "status": "PAID"}}
                                """
                                        .formatted(ASAAS_CHECKOUT_ID)))
                .andExpect(status().isOk());

        // Verify order is now PAID
        Order paidOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);

        // Verify wallet balance was credited
        Wallet walletAfterPayment =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assertThat(walletAfterPayment.getBalance()).isEqualTo(initialBalance + orderAmount);

        // === STEP 3: Couple sees available balance (fee-adjusted) ===
        int grossBalance = walletAfterPayment.getBalance();
        int expectedAvailable = grossBalance * 10000 / (10000 + walletAfterPayment.getFee());

        mockMvc.perform(get("/wallet/{id}", WALLET_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available_balance").value(expectedAvailable));

        // === STEP 4: Couple requests transfer ===
        int transferAmount = expectedAvailable;

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": %d}".formatted(transferAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(transferAmount))
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Wallet balance NOT yet deducted
        Wallet walletAfterTransferRequest =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assertThat(walletAfterTransferRequest.getBalance()).isEqualTo(grossBalance);

        // === STEP 5: Asaas sends TRANSFER_DONE webhook ===
        mockMvc.perform(post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {"id": "evt_2", "event": "TRANSFER_DONE", "transfer": {"id": "%s"}}
                                """
                                        .formatted(ASAAS_TRANSFER_ID)))
                .andExpect(status().isOk());

        // Verify transaction is COMPLETED
        var transaction =
                transactionRepository.findByAsaasTransferId(ASAAS_TRANSFER_ID).orElseThrow();
        assertThat(transaction.getStatus()).isEqualTo(com.hestia.api.domain.payment.enums.TransactionStatus.COMPLETED);

        // Verify wallet balance deducted (amount + fee)
        Wallet walletFinal =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assertThat(walletFinal.getBalance()).isEqualTo(grossBalance - transaction.getAmount() - transaction.getFee());
    }

    @Test
    @DisplayName("Transfer fails: balance is untouched")
    @WithMockCouple
    void transferFailedKeepsBalance() throws Exception {
        Wallet walletBefore =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        int balanceBefore = walletBefore.getBalance();

        // Create transfer
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5000}"))
                .andExpect(status().isOk());

        // Asaas reports failure
        mockMvc.perform(post("/webhook/asaas/{token}", WEBHOOK_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {"id": "evt_3", "event": "TRANSFER_FAILED", "transfer": {"id": "%s"}}
                                """
                                        .formatted(ASAAS_TRANSFER_ID)))
                .andExpect(status().isOk());

        // Balance unchanged
        Wallet walletAfter =
                walletRepository.findById(UUID.fromString(WALLET_ID)).orElseThrow();
        assertThat(walletAfter.getBalance()).isEqualTo(balanceBefore);

        // Transaction marked FAILED
        var transaction =
                transactionRepository.findByAsaasTransferId(ASAAS_TRANSFER_ID).orElseThrow();
        assertThat(transaction.getStatus()).isEqualTo(com.hestia.api.domain.payment.enums.TransactionStatus.FAILED);
    }
}
