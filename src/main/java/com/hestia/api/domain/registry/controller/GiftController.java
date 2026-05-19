package com.hestia.api.domain.registry.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.registry.dto.CreateGiftRequest;
import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.dto.GiftResponse;
import com.hestia.api.domain.registry.dto.UpdateGiftRequest;
import com.hestia.api.domain.registry.service.GiftService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/gift")
@Tag(name = "Registry", description = "CRUD operations for registries management")
@RequiredArgsConstructor
public class GiftController {

    private final GiftService giftService;

    @GetMapping
    public ResponseEntity<PageResponse<GiftAvailabilityResponse>> getGift(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable
    ) {
        return ResponseEntity.ok(giftService.getGifts(user.resolveWeddingId(wedding), pageable));
    }

    @PostMapping
    public ResponseEntity<GiftResponse> postGift(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateGiftRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(giftService.createGift(user.getWeddingId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftAvailabilityResponse> getGiftById(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(giftService.getGiftById(user.getWeddingId(), isAdmin, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftResponse> patchGift(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGiftRequest request
    ) {
        return ResponseEntity.ok(giftService.updateGift(user.getWeddingId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGift(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        giftService.deleteGift(user.getWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
