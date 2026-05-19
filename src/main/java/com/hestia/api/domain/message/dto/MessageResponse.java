package com.hestia.api.domain.message.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record MessageResponse(
        UUID id, String sender, String message, Boolean isFavorite, Boolean isNew, LocalDateTime createdAt) {}
