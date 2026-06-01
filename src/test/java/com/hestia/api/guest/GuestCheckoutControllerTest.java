package com.hestia.api.guest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hestia.api.infrastructure.asaas.AsaasCheckoutClient;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutResponse;

import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Guest Checkout")
class GuestCheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsaasCheckoutClient asaasCheckoutClient;

    private static final String VALID_SLUG = "alice-bob";
    private static final String INVALID_SLUG = "nonexistent";
    private static final String GIFT_A = "eeee0000-0000-0000-0000-000000000001";
    private static final String NONEXISTENT_GIFT = "00000000-0000-0000-0000-000000000099";

    @BeforeEach
    void setUp() {
        AsaasCheckoutResponse response = new AsaasCheckoutResponse();
        response.setId("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
        response.setLink("https://sandbox.asaas.com/checkoutSession/show/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
        response.setStatus("ACTIVE");
        when(asaasCheckoutClient.createCheckout(any())).thenReturn(response);
    }

    @Test
    void createsCheckoutSuccessfully() throws Exception {
        mockMvc.perform(post("/w/{slug}/gift/{giftId}/checkout", VALID_SLUG, GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"guest_name\": \"João Silva\", \"guest_email\": \"joao@email.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkout_url")
                        .value("https://sandbox.asaas.com/checkoutSession/show/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"));
    }

    @Test
    void returns404ForInvalidSlug() throws Exception {
        mockMvc.perform(post("/w/{slug}/gift/{giftId}/checkout", INVALID_SLUG, GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"guest_name\": \"João\", \"guest_email\": \"joao@email.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404ForNonexistentGift() throws Exception {
        mockMvc.perform(post("/w/{slug}/gift/{giftId}/checkout", VALID_SLUG, NONEXISTENT_GIFT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"guest_name\": \"João\", \"guest_email\": \"joao@email.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns400WhenMissingFields() throws Exception {
        mockMvc.perform(post("/w/{slug}/gift/{giftId}/checkout", VALID_SLUG, GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
