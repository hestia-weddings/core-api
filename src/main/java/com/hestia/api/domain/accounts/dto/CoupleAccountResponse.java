package com.hestia.api.domain.accounts.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CoupleAccountResponse(UUID id, String name, String email, LocalDateTime createdAt) {}
