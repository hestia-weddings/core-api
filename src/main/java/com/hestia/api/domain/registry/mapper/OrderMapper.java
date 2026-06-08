package com.hestia.api.domain.registry.mapper;

import com.hestia.api.domain.registry.dto.OrderResponse;
import com.hestia.api.domain.registry.entity.Order;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final GiftMapper giftMapper;

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .guestName(order.getGuestName())
                .guestEmail(order.getGuestName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .paymentId(order.getPaymentId())
                .gift(giftMapper.toResponse(order.getGift()))
                .createdAt(order.getCreatedAt())
                .build();
    }
}
