package com.hestia.api.domain.registry.mapper;

import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.entity.Gift;
import com.hestia.api.domain.registry.entity.GiftAvailability;

import org.springframework.stereotype.Component;

@Component
public class GiftMapper {

    public GiftResponse toResponse(Gift gift) {
        return GiftResponse.builder()
                .id(gift.getId())
                .description(gift.getDescription())
                .picture(gift.getPicture())
                .price(gift.getPrice())
                .stock(gift.getStock())
                .createdAt(gift.getCreatedAt())
                .build();
    }

    public GiftAvailabilityResponse toAvailabilityResponse(GiftAvailability giftAvailability) {
        return GiftAvailabilityResponse.builder()
                .id(giftAvailability.getId())
                .description(giftAvailability.getDescription())
                .picture(giftAvailability.getPicture())
                .price(giftAvailability.getPrice())
                .stock(giftAvailability.getStock())
                .remain(giftAvailability.getRemain())
                .availability(giftAvailability.getAvailability())
                .createdAt(giftAvailability.getCreatedAt())
                .build();
    }
}
