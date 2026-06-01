package com.hestia.api.infrastructure.asaas;

import com.hestia.api.infrastructure.asaas.dto.AsaasTransferRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferResponse;
import com.hestia.api.infrastructure.asaas.enums.AsaasEnvironment;
import com.hestia.api.infrastructure.asaas.exception.AsaasTransferException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AsaasTransferClient {

    private final RestClient restClient;
    private final ObjectMapper asaasMapper;
    private final String apiKey;
    private final AsaasEnvironment environment;

    public AsaasTransferClient(
            @Value("${asaas.api-key}") String apiKey, @Value("${asaas.environment}") AsaasEnvironment environment) {
        this.asaasMapper = new ObjectMapper();
        this.restClient = RestClient.builder().build();
        this.apiKey = apiKey;
        this.environment = environment;
    }

    public AsaasTransferResponse createTransfer(AsaasTransferRequest request) {
        String baseUrl = environment == AsaasEnvironment.PRODUCTION
                ? "https://api.asaas.com/v3"
                : "https://api-sandbox.asaas.com/v3";

        try {
            String json = asaasMapper.writeValueAsString(request);
            String response = restClient
                    .post()
                    .uri(baseUrl + "/transfers")
                    .header("access_token", apiKey)
                    .header("User-Agent", "Hestia/1.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .body(String.class);
            return asaasMapper.readValue(response, AsaasTransferResponse.class);
        } catch (AsaasTransferException e) {
            throw e;
        } catch (Exception e) {
            throw new AsaasTransferException("Failed to create Asaas transfer: " + e.getMessage());
        }
    }
}
