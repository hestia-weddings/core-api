package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateGuestRequest;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.UpdateGuestRequest;
import com.hestia.api.domain.rsvp.dto.UpdateGuestStatusRequest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.service.GuestService;
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
@RequestMapping("/rsvp/guest")
@Tag(name = "RSVP", description = "CRUD operations for rsvp module managements")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public ResponseEntity<PageResponse<GuestResponse>> getGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            Pageable pageable,
            @RequestParam(required = false, name = "status") GuestStatus status,
            @RequestParam(required = false, name = "invite_id") UUID inviteId
    ) {
        return ResponseEntity.ok(guestService.getGuests(user.getWeddingId(), pageable, status, inviteId));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> postGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateGuestRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.createGuest(user.getWeddingId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GuestResponse> patchGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGuestRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuest(user.getWeddingId(), id, request));
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<GuestResponse> patchGuestStatus(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGuestStatusRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuestStatus(user.getWeddingId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        guestService.deleteGuest(user.getWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
