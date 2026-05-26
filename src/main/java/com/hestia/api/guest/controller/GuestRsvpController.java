package com.hestia.api.guest.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.SearchInviteRequest;
import com.hestia.api.domain.rsvp.dto.UpdateGuestStatusRequest;
import com.hestia.api.domain.rsvp.service.GuestService;
import com.hestia.api.domain.rsvp.service.InviteService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/w/{slug}/rsvp")
@Tag(name = "Guest", description = "Public guest-facing endpoints")
@RequiredArgsConstructor
public class GuestRsvpController {

    private final InviteService inviteService;
    private final GuestService guestService;

    @PostMapping("/invite/search")
    public ResponseEntity<InviteResponse> searchInvite(
            @RequestAttribute UUID weddingId, @Valid @RequestBody SearchInviteRequest request) {
        return ResponseEntity.ok(inviteService.searchInvite(weddingId, request));
    }

    @GetMapping("/guest")
    public ResponseEntity<PageResponse<GuestResponse>> getGuest(
            @RequestAttribute UUID weddingId, Pageable pageable, @RequestParam(name = "invite_id") UUID inviteId) {
        return ResponseEntity.ok(guestService.getGuests(weddingId, null, inviteId, pageable));
    }

    @PatchMapping("/guest/status/{id}")
    public ResponseEntity<GuestResponse> patchGuestStatus(
            @RequestAttribute UUID weddingId,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGuestStatusRequest request) {
        return ResponseEntity.ok(guestService.updateGuestStatus(weddingId, id, request));
    }
}
