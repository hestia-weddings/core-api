package com.hestia.api.infrastructure.asaas;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.hestia.api.domain.payment.enums.PaymentEnvironment;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutResponse;
import com.hestia.api.infrastructure.asaas.exception.AsaasCheckoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

@DisplayName("Asaas Checkout Client")
class AsaasCheckoutClientTest {

    private MockRestServiceServer mockServer;
    private AsaasCheckoutClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new AsaasCheckoutClient(builder);
    }

    private AsaasCheckoutRequest buildRequest() {
        return AsaasCheckoutRequest.builder()
                .billingTypes(List.of("PIX", "CREDIT_CARD"))
                .chargeTypes(List.of("DETACHED"))
                .minutesToExpire(60)
                .externalReference("order-uuid-123")
                .callback(AsaasCheckoutRequest.Callback.builder()
                        .successUrl("https://hestia.com/success")
                        .cancelUrl("https://hestia.com/cancel")
                        .expiredUrl("https://hestia.com/expired")
                        .build())
                .items(List.of(AsaasCheckoutRequest.Item.builder()
                        .name("Jogo de Panelas")
                        .description("Gift from registry")
                        .quantity(1)
                        .value(250.00)
                        .build()))
                .customerData(AsaasCheckoutRequest.CustomerData.builder()
                        .name("João Silva")
                        .email("joao@email.com")
                        .build())
                .build();
    }

    @Test
    void createsCheckoutSuccessfully() {
        String responseJson =
                """
                {"id": "abc-123", "link": "https://sandbox.asaas.com/checkoutSession/show/abc-123", "status": "ACTIVE"}
                """;

        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/checkouts"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("access_token", "test-key"))
                .andExpect(header("User-Agent", "Hestia/1.0"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        AsaasCheckoutResponse response = client.createCheckout("test-key", PaymentEnvironment.SANDBOX, buildRequest());

        assertNotNull(response);
        assertEquals("abc-123", response.getId());
        assertEquals("https://sandbox.asaas.com/checkoutSession/show/abc-123", response.getLink());
        assertEquals("ACTIVE", response.getStatus());
        mockServer.verify();
    }

    @Test
    void usesProductionUrlWhenEnvironmentIsProduction() {
        String responseJson =
                """
                {"id": "prod-456", "link": "https://asaas.com/checkoutSession/show/prod-456", "status": "ACTIVE"}
                """;

        mockServer
                .expect(requestTo("https://api.asaas.com/v3/checkouts"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("access_token", "prod-key"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        AsaasCheckoutResponse response =
                client.createCheckout("prod-key", PaymentEnvironment.PRODUCTION, buildRequest());

        assertEquals("prod-456", response.getId());
        mockServer.verify();
    }

    @Test
    void throwsExceptionOnUnauthorized() {
        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/checkouts"))
                .andRespond(withUnauthorizedRequest());

        assertThrows(
                AsaasCheckoutException.class,
                () -> client.createCheckout("invalid-key", PaymentEnvironment.SANDBOX, buildRequest()));
        mockServer.verify();
    }

    @Test
    void throwsExceptionOnBadRequest() {
        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/checkouts"))
                .andRespond(withBadRequest());

        assertThrows(
                AsaasCheckoutException.class,
                () -> client.createCheckout("test-key", PaymentEnvironment.SANDBOX, buildRequest()));
        mockServer.verify();
    }

    @Test
    void throwsExceptionOnServerError() {
        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/checkouts"))
                .andRespond(withServerError());

        assertThrows(
                AsaasCheckoutException.class,
                () -> client.createCheckout("test-key", PaymentEnvironment.SANDBOX, buildRequest()));
        mockServer.verify();
    }
}
