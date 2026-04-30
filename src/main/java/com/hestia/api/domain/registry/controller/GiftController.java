package com.hestia.api.domain.registry.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.registry.dto.CreateGiftRequest;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.dto.UpdateGiftRequest;
import com.hestia.api.domain.registry.service.GiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/gift")
@Tag(name = "Gifts", description = "CRUD operations for gifts")
@RequiredArgsConstructor
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping
    public PageResponse<GiftAvailabilityResponse> getGift(Pageable pageable) {
        return giftService.getGifts(pageable);
    }

    @PostMapping
    public GiftResponse postGift(@RequestBody CreateGiftRequest request) {
        return giftService.createGift(request);
    }

    @PatchMapping("/{id}")
    public GiftResponse patchGift(
            @PathVariable UUID id,
            @RequestBody UpdateGiftRequest request
    ) {
        return giftService.updateGift(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteGift(@PathVariable UUID id) { giftService.deleteGift(id); }
}
