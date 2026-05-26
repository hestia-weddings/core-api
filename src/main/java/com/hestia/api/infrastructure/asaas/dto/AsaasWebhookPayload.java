package com.hestia.api.infrastructure.asaas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsaasWebhookPayload {

    private String id;
    private String event;
    private Checkout checkout;

    @Getter
    @Setter
    public static class Checkout {
        private String id;
        private String status;
    }
}
