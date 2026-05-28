package com.hestia.api.domain.payment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WalletResponse(
        UUID id, String pixKey, Integer balance, Integer fee, LocalDateTime createdAt) {}
