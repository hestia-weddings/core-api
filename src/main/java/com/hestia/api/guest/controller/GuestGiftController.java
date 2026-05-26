package com.hestia.api.guest.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.service.GiftService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/w/{slug}/gift")
@Tag(name = "Guest", description = "Public guest-facing endpoints")
@RequiredArgsConstructor
public class GuestGiftController {

    private final GiftService giftService;

    @GetMapping
    public ResponseEntity<PageResponse<GiftAvailabilityResponse>> getGift(
            @RequestAttribute UUID weddingId, Pageable pageable) {
        return ResponseEntity.ok(giftService.getGifts(weddingId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftAvailabilityResponse> getGiftById(
            @RequestAttribute UUID weddingId, @PathVariable UUID id) {
        return ResponseEntity.ok(giftService.getGiftById(weddingId, id));
    }
}
