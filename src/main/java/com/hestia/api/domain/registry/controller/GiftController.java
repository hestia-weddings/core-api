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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PageResponse<GiftAvailabilityResponse>> getGift(Pageable pageable) {
        return ResponseEntity.ok(giftService.getGifts(pageable));
    }

    @PostMapping
    public ResponseEntity<GiftResponse> postGift(@RequestBody CreateGiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(giftService.createGift(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftResponse> patchGift(
            @PathVariable UUID id,
            @RequestBody UpdateGiftRequest request
    ) {
        return ResponseEntity.ok(giftService.updateGift(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGift(@PathVariable UUID id) {
        giftService.deleteGift(id);
        return ResponseEntity.noContent().build();
    }
}
