package com.hestia.api.domain.registry.mapper;

import com.hestia.api.domain.registry.dto.OrderResponse;
import com.hestia.api.domain.registry.entity.Order;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .guestName(order.getGuestName())
                .guestEmail(order.getGuestName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .paymentId(order.getPaymentId())
                .giftId(order.getGift().getId())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
