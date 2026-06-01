package com.hestia.api.domain.payment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record TransferResponse(UUID id, Integer amount, String status, LocalDateTime createdAt) {}
