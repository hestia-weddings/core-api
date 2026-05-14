package com.hestia.api.guest;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.service.GiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/w/{slug}/gift")
@Tag(name = "Guest Registry", description = "CRUD operations for public registries view")
@RequiredArgsConstructor
public class GuestGiftController {

    private final GiftService giftService;

    @GetMapping
    public ResponseEntity<PageResponse<GiftAvailabilityResponse>> getGift(Pageable pageable) {
        return ResponseEntity.ok(giftService.getGifts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftAvailabilityResponse> getGiftById(@PathVariable UUID id) {
        return ResponseEntity.ok(giftService.getGiftById(id));
    }
}
