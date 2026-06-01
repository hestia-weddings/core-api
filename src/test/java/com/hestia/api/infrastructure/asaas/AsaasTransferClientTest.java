package com.hestia.api.infrastructure.asaas;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.hestia.api.infrastructure.asaas.dto.AsaasTransferRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferResponse;
import com.hestia.api.infrastructure.asaas.enums.AsaasEnvironment;
import com.hestia.api.infrastructure.asaas.exception.AsaasTransferException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@DisplayName("Asaas Transfer Client")
class AsaasTransferClientTest {

    private MockRestServiceServer mockServer;
    private AsaasTransferClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new AsaasTransferClient("test-key", AsaasEnvironment.SANDBOX);
    }

    @Test
    void createsTransferSuccessfully() {
        String responseJson =
                """
                {"id": "transfer-123", "status": "PENDING", "value": 100.00}
                """;

        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/transfers"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("access_token", "test-key"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        AsaasTransferRequest request =
                new AsaasTransferRequest(BigDecimal.valueOf(100), "PIX", "pix@test.com", "EMAIL");
        AsaasTransferResponse response = client.createTransfer(request);

        assertNotNull(response);
        assertEquals("transfer-123", response.id());
        assertEquals("PENDING", response.status());
        mockServer.verify();
    }

    @Test
    void throwsExceptionOnServerError() {
        mockServer
                .expect(requestTo("https://api-sandbox.asaas.com/v3/transfers"))
                .andRespond(withServerError());

        AsaasTransferRequest request =
                new AsaasTransferRequest(BigDecimal.valueOf(100), "PIX", "pix@test.com", "EMAIL");
        assertThrows(AsaasTransferException.class, () -> client.createTransfer(request));
        mockServer.verify();
    }
}
