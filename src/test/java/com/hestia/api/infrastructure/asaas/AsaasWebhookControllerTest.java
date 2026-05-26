package com.hestia.api.infrastructure.asaas;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Asaas Webhook")
class AsaasWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_TOKEN = "wh_test_token_abc123def456ghi789jkl012mno345pqr678st";
    private static final String INVALID_TOKEN = "invalid_token_xyz";
    private static final String PAYMENT_ID = "cccc1111-0000-0000-0000-000000000001";

    private String webhookPayload(String event, String checkoutId) {
        return """
                {
                    "id": "evt_test123",
                    "event": "%s",
                    "checkout": {
                        "id": "%s",
                        "status": "PAID"
                    }
                }
                """.formatted(event, checkoutId);
    }

    @Test
    void processesCheckoutPaidSuccessfully() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_PAID", PAYMENT_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void processesCheckoutExpired() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_EXPIRED", PAYMENT_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void processesCheckoutCanceled() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_CANCELED", PAYMENT_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void returns404ForInvalidToken() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", INVALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_PAID", PAYMENT_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404ForUnknownPaymentId() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_PAID", "00000000-0000-0000-0000-000000000099")))
                .andExpect(status().isNotFound());
    }

    @Test
    void idempotentOnDuplicateEvent() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_PAID", PAYMENT_ID)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_PAID", PAYMENT_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void ignoresUnknownEvents() throws Exception {
        mockMvc.perform(post("/webhook/asaas/{token}", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload("CHECKOUT_CREATED", PAYMENT_ID)))
                .andExpect(status().isOk());
    }
}
