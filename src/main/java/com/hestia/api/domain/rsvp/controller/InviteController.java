package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateInviteRequest;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.SearchInviteRequest;
import com.hestia.api.domain.rsvp.dto.UpdateInviteRequest;
import com.hestia.api.domain.rsvp.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rsvp/invite")
@Tag(name = "RSVP")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @GetMapping
    public ResponseEntity<PageResponse<InviteResponse>> getInvite(Pageable pageable) {
        return ResponseEntity.ok(inviteService.getInvites(pageable));
    }

    @PostMapping
    public ResponseEntity<InviteResponse> postInvite(@RequestBody CreateInviteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inviteService.createInvite(request));
    }

    @PostMapping("/search")
    public ResponseEntity<InviteResponse> searchInvite(@RequestBody SearchInviteRequest request) {
        return ResponseEntity.ok(inviteService.searchInvite(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InviteResponse> patchInvite(
            @PathVariable UUID id,
            @RequestBody UpdateInviteRequest request
    ) {
        return ResponseEntity.ok(inviteService.updateInvite(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvite(@PathVariable UUID id) {
        inviteService.deleteInvite(id);
        return ResponseEntity.noContent().build();
    }
}
