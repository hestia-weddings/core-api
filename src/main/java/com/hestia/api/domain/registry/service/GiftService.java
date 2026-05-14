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
import com.hestia.api.domain.registry.mapper.GiftMapper;
import com.hestia.api.domain.registry.repository.GiftAvailabilityRepository;
import com.hestia.api.domain.registry.repository.GiftRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftService {

    private final GiftRepository giftRepository;
    private final GiftAvailabilityRepository giftAvailabilityRepository;
    private final GiftMapper giftMapper;

    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<GiftAvailabilityResponse> getGifts(Pageable pageable) {
        Page<GiftAvailabilityResponse> page = giftAvailabilityRepository.findAll(pageable)
                .map(giftMapper::toAvailabilityResponse);

        return PageMapper.toResponse(page);
    }

    @Transactional(readOnly = true)
    public GiftAvailabilityResponse getGiftById(UUID id) {
        GiftAvailability gift = giftAvailabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));

        return giftMapper.toAvailabilityResponse(gift);
    }

    private Gift getGift(UUID id) {
        return giftRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));
    }

    public GiftResponse createGift(CreateGiftRequest request) {
        Wedding wedding = weddingRepository.findByIdAndIsActiveTrue(request.getWeddingId())
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        Gift gift = Gift.builder()
                .description(request.getDescription())
                .picture(request.getPicture())
                .price(request.getPrice())
                .stock(request.getStock())
                .wedding(wedding)
                .build();

        return giftMapper.toResponse(giftRepository.save(gift));
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

        return giftMapper.toResponse(giftRepository.save(gift));
    }

    public void deleteGift(UUID id) {
        Gift gift = getGift(id);
        gift.setIsActive(false);
        giftRepository.save(gift);
    }
}
