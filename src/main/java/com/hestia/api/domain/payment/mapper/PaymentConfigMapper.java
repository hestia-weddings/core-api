package com.hestia.api.domain.payment.mapper;

import com.hestia.api.domain.payment.dto.PaymentConfigResponse;
import com.hestia.api.domain.payment.entity.PaymentConfig;

import org.springframework.stereotype.Component;

@Component
public class PaymentConfigMapper {

    public PaymentConfigResponse toResponse(PaymentConfig paymentConfig) {
        return PaymentConfigResponse.builder()
                .id(paymentConfig.getId())
                .apiKey(paymentConfig.getApiKey())
                .environment(paymentConfig.getEnvironment())
                .webhookToken(paymentConfig.getWebhookToken())
                .createdAt(paymentConfig.getCreatedAt())
                .build();
    }
}
