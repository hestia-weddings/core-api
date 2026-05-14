package com.hestia.api.guest;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.SearchInviteRequest;
import com.hestia.api.domain.rsvp.dto.UpdateGuestStatusRequest;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.service.GuestService;
import com.hestia.api.domain.rsvp.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/w/{slug}/rsvp")
@Tag(name = "Guest RSVP", description = "CRUD operations for rsvp module managements")
@RequiredArgsConstructor
public class GuestRsvpController {

    private final InviteService inviteService;
    private final GuestService guestService;

    @PostMapping("/invite/search")
    public ResponseEntity<InviteResponse> searchInvite(@Valid @RequestBody SearchInviteRequest request) {
        return ResponseEntity.ok(inviteService.searchInvite(request));
    }

    @GetMapping("/guest")
    public ResponseEntity<PageResponse<GuestResponse>> getGuest(
            Pageable pageable,
            @RequestParam(required = false, name = "status") GuestStatus status,
            @RequestParam(required = false, name = "invite_id") Invite invite
    ) {
        return ResponseEntity.ok(guestService.getGuests(pageable, status, invite));
    }

    @PatchMapping("/guest/status/{id}")
    public ResponseEntity<GuestResponse> patchGuestStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGuestStatusRequest request
    ) {
        return ResponseEntity.ok(guestService.updateGuestStatus(id, request));
    }
}
