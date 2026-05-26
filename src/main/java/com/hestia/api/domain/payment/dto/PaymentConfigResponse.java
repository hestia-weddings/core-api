package com.hestia.api.domain.payment.dto;

import com.hestia.api.domain.payment.enums.PaymentEnvironment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentConfigResponse(
        UUID id, String apiKey, PaymentEnvironment environment, String webhookToken, LocalDateTime createdAt) {}
