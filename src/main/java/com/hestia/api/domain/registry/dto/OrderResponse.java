package com.hestia.api.domain.registry.dto;

import com.hestia.api.domain.registry.enums.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,
        String guestName,
        String guestEmail,
        Integer amount,
        OrderStatus status,
        UUID paymentId,
        UUID giftId,
        LocalDateTime createdAt) {}
