package com.hestia.api.domain.registry.controller;

import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.dto.UpdateGiftRequest;
import com.hestia.api.domain.registry.service.GiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gift")
@Tag(name = "Gifts", description = "CRUD operations for gifts")
@RequiredArgsConstructor
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping
    public List<GiftAvailabilityResponse> getGift() {
        return giftService.getGifts();
    }

    @PostMapping
    public void postGift() {}

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
