package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateGuestRequest;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.UpdateGuestRequest;
import com.hestia.api.domain.rsvp.dto.UpdateGuestStatusRequest;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.service.GuestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            Pageable pageable,
            @RequestParam(required = false, name = "status") GuestStatus status,
            @RequestParam(required = false, name = "invite_id") Invite invite
    ) {
        return ResponseEntity.ok(guestService.getGuests(pageable, status, invite));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> postGuest(@RequestBody CreateGuestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.createGuest(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GuestResponse> patchGuest(
            @PathVariable UUID id,
            @RequestBody UpdateGuestRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuest(id, request));
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<GuestResponse> patchGuestStatus(
            @PathVariable UUID id,
            @RequestBody UpdateGuestStatusRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuestStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable UUID id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }
}
