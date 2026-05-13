package com.hestia.api.domain.registry.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GiftResponse(
        UUID id,
        String description,
        String picture,
        Integer price,
        Integer stock,
        LocalDateTime createdAt
) {}
