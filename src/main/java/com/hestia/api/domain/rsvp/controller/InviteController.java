package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateInviteRequest;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.UpdateInviteRequest;
import com.hestia.api.domain.rsvp.service.InviteService;
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
@RequestMapping("/rsvp/invite")
@Tag(name = "RSVP")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @GetMapping
    public ResponseEntity<PageResponse<InviteResponse>> getInvite(
            @AuthenticationPrincipal AuthenticatedUser user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(inviteService.getInvites(user.getWeddingId(), pageable));
    }

    @PostMapping
    public ResponseEntity<InviteResponse> postInvite(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateInviteRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inviteService.createInvite(user.getWeddingId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InviteResponse> patchInvite(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInviteRequest request
    ) {
        return ResponseEntity.ok(inviteService.updateInvite(user.getWeddingId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvite(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        inviteService.deleteInvite(user.getWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
