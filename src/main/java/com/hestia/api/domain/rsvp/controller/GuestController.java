package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateGuestRequest;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.UpdateGuestRequest;
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
            @RequestParam(required = false, name = "status") GuestStatus status,
            @RequestParam(required = false, name = "invite_id") UUID inviteId,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable
    ) {
        return ResponseEntity.ok(guestService.getGuests(user.resolveWeddingId(wedding), status, inviteId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(guestService.getGuestById(user.resolveWeddingId(), id));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> postGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            @Valid @RequestBody CreateGuestRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.createGuest(user.resolveWeddingId(wedding), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GuestResponse> patchGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGuestRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuest(user.resolveWeddingId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        guestService.deleteGuest(user.resolveWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
