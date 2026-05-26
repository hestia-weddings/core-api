package com.hestia.api.infrastructure.asaas;

import com.hestia.api.domain.payment.enums.PaymentEnvironment;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutResponse;
import com.hestia.api.infrastructure.asaas.exception.AsaasCheckoutException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AsaasCheckoutClient {

    private final RestClient restClient;

    public AsaasCheckoutClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public AsaasCheckoutResponse createCheckout(
            String apiKey, PaymentEnvironment environment, AsaasCheckoutRequest request) {
        String baseUrl = environment == PaymentEnvironment.PRODUCTION
                ? "https://api.asaas.com/v3"
                : "https://api-sandbox.asaas.com/v3";

        try {
            return restClient
                    .post()
                    .uri(baseUrl + "/checkouts")
                    .header("access_token", apiKey)
                    .header("User-Agent", "Hestia/1.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AsaasCheckoutResponse.class);
        } catch (Exception e) {
            throw new AsaasCheckoutException("Failed to create Asaas checkout: " + e.getMessage());
        }
    }
}
