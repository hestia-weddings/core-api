package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateInviteRequest;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.UpdateInviteRequest;
import com.hestia.api.domain.rsvp.dto.UpdateInviteStatusRequest;
import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
import com.hestia.api.domain.rsvp.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rsvp/guest")
@Tag(name = "RSVP", description = "CRUD operations for rsvp module managements")
@RequiredArgsConstructor
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @GetMapping
    public ResponseEntity<PageResponse<InviteResponse>> getInvite(
            Pageable pageable,
            @RequestParam(required = false, name = "status") InviteStatus status,
            @RequestParam(required = false, name = "household_id") Household household
    ) {
        return ResponseEntity.ok(inviteService.getInvites(pageable, status, household));
    }

    @PostMapping
    public ResponseEntity<InviteResponse> postInvite(@RequestBody CreateInviteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inviteService.createInvite(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InviteResponse> patchInvite(
            @PathVariable UUID id,
            @RequestBody UpdateInviteRequest request
    ) {
        return ResponseEntity.ok(inviteService.updateInvite(id, request));
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<InviteResponse> patchInviteStatus(
            @PathVariable UUID id,
            @RequestBody UpdateInviteStatusRequest request
    ) {
        return ResponseEntity.ok(inviteService.updateInviteStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvite(@PathVariable UUID id) {
        inviteService.deleteInvite(id);
        return ResponseEntity.noContent().build();
    }
}
