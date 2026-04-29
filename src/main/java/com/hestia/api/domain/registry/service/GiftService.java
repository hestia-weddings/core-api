package com.hestia.api.domain.registry.service;

import com.hestia.api.domain.registry.dto.CreateGiftRequest;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.dto.UpdateGiftRequest;
import com.hestia.api.domain.registry.entity.Gift;
import com.hestia.api.domain.registry.entity.GiftAvailability;
import com.hestia.api.domain.registry.repository.GiftAvailabilityRepository;
import com.hestia.api.domain.registry.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GiftService {

    private final GiftRepository giftRepository;
    private final GiftAvailabilityRepository giftAvailabilityRepository;

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

    public List<GiftAvailabilityResponse> getGifts() {
        List<GiftAvailability> giftAvailabilities;

        giftAvailabilities = giftAvailabilityRepository.findAll();

        return giftAvailabilities.stream()
                .map(this::toAvailabilityResponse)
                .toList();
    }

    public Gift getGiftById(UUID id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gift not found"));
    }

    public GiftResponse createGift(CreateGiftRequest request) {
        Gift gift = Gift.builder()
                .description(request.getDescription())
                .picture(request.getPicture())
                .price(request.getPrice())
                .stock(request.getStock())
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        return this.toResponse(giftRepository.save(gift));
    }

    public GiftResponse updateGift(UUID id, UpdateGiftRequest request) {
        Gift gift = getGiftById(id);

        if (request.getDescription() != null)
            gift.setDescription(request.getDescription());
        if (request.getPicture() != null)
            gift.setPicture(request.getPicture());
        if (request.getPrice() != null)
            gift.setPrice(request.getPrice());
        if (request.getStock() != null)
            gift.setStock(request.getStock());

        return this.toResponse(giftRepository.save(gift));
    }

    public void deleteGift(UUID id) {
        Gift gift = getGiftById(id);

        gift.setIsActive(false);

        giftRepository.save(gift);
    }
}
