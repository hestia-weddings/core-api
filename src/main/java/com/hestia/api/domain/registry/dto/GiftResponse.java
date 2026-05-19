package com.hestia.api.domain.registry.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GiftResponse(
        UUID id, String description, String picture, Integer price, Integer stock, LocalDateTime createdAt) {}
