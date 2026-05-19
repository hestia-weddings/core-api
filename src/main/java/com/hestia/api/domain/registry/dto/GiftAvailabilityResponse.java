package com.hestia.api.domain.registry.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GiftAvailabilityResponse(
        UUID id,
        String description,
        String picture,
        Integer price,
        Integer stock,
        Integer remain,
        Boolean availability,
        LocalDateTime createdAt) {}
