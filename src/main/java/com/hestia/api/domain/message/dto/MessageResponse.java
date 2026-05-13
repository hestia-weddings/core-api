package com.hestia.api.domain.message.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MessageResponse(
        UUID id,
        String sender,
        String message,
        Boolean isFavorite,
        Boolean isNew,
        LocalDateTime createdAt
) {}
