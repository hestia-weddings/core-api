package com.hestia.api.domain.wedding.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.wedding.dto.CreateWeddingRequest;
import com.hestia.api.domain.wedding.dto.UpdateWeddingRequest;
import com.hestia.api.domain.wedding.dto.WeddingResponse;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.mapper.WeddingMapper;
import com.hestia.api.domain.wedding.repository.WeddingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WeddingService {

    private final WeddingRepository weddingRepository;
    private final WeddingMapper weddingMapper;

    @Transactional(readOnly = true)
    public PageResponse<WeddingResponse> getWeddings(Pageable pageable) {
        Page<WeddingResponse> page =
                weddingRepository.findByIsActiveTrue(pageable).map(weddingMapper::toResponse);

        return PageMapper.toResponse(page);
    }

    @Transactional(readOnly = true)
    public WeddingResponse getWeddingById(UUID id) {
        Wedding page =
                weddingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        return weddingMapper.toResponse(page);
    }

    private Wedding getWedding(UUID id) {
        return weddingRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));
    }

    public WeddingResponse createWedding(CreateWeddingRequest request) {
        Wedding wedding = Wedding.builder()
                .coupleName(request.getCoupleName())
                .date(request.getDate())
                .inviteMessage(request.getInviteMessage())
                .giftMessage(request.getGiftMessage())
                .slug(request.getSlug())
                .build();

        return weddingMapper.toResponse(weddingRepository.save(wedding));
    }

    public WeddingResponse updateWedding(UUID id, UpdateWeddingRequest request) {
        Wedding wedding = getWedding(id);

        if (request.getCoupleName() != null) wedding.setCoupleName(request.getCoupleName());
        if (request.getDate() != null) wedding.setDate(request.getDate());
        if (request.getInviteMessage() != null) wedding.setInviteMessage(request.getInviteMessage());
        if (request.getGiftMessage() != null) wedding.setGiftMessage(request.getGiftMessage());

        return weddingMapper.toResponse(weddingRepository.save(wedding));
    }

    public void deleteWedding(UUID id) {
        Wedding wedding = getWedding(id);
        wedding.setIsActive(false);
        weddingRepository.save(wedding);
    }
}
