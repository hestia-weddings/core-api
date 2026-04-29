package com.hestia.api.domain.registry.service;

import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.entity.Gift;
import com.hestia.api.domain.registry.entity.GiftAvailability;
import com.hestia.api.domain.registry.repository.GiftAvailabilityRepository;
import com.hestia.api.domain.registry.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftService {

    private final GiftRepository giftRepository;
    private final GiftAvailabilityRepository giftAvailabilityRepository;

    public GiftResponse toResponse(Gift gift) {
        return GiftResponse.builder()
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

    public List<GiftAvailabilityResponse> getGifts() {
        List<GiftAvailability> giftAvailabilities;

        giftAvailabilities = giftAvailabilityRepository.findAll();

        return giftAvailabilities.stream()
                .map(this::toAvailabilityResponse)
                .toList();
    }
}
