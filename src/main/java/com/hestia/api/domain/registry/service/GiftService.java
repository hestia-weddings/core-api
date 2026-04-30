package com.hestia.api.domain.registry.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.registry.dto.CreateGiftRequest;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.dto.UpdateGiftRequest;
import com.hestia.api.domain.registry.entity.Gift;
import com.hestia.api.domain.registry.entity.GiftAvailability;
import com.hestia.api.domain.registry.repository.GiftAvailabilityRepository;
import com.hestia.api.domain.registry.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public PageResponse<GiftAvailabilityResponse> getGifts(Pageable pageable) {
        Page<GiftAvailabilityResponse> page = giftAvailabilityRepository.findAll(pageable)
                .map(this::toAvailabilityResponse);

        return PageMapper.toResponse(page);
    }

    public GiftAvailabilityResponse getGiftById(UUID id) {
        GiftAvailability gift = giftAvailabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));

        return toAvailabilityResponse(gift);
    }

    private Gift getGift(UUID id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));
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
        Gift gift = getGift(id);

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
        Gift gift = getGift(id);

        gift.setIsActive(false);

        giftRepository.save(gift);
    }
}
