package com.hestia.api.domain.registry.dto;

import com.hestia.api.domain.registry.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderResponse(
        UUID id,
        String guestName,
        String guestEmail,
        Integer amount,
        OrderStatus status,
        UUID paymentId,
        GiftResponse gift,
        LocalDateTime createdAt) {}
