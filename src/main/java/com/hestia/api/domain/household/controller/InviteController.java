package com.hestia.api.domain.household.controller;

import com.hestia.api.domain.household.dto.CreateInviteRequest;
import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.dto.UpdateInviteRequest;
import com.hestia.api.domain.household.dto.UpdateInviteStatusRequest;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.enums.InviteStatus;
import com.hestia.api.domain.household.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invite")
@Tag(name = "Invites", description = "CRUD operations for invites")
@RequiredArgsConstructor
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @GetMapping
    public List<InviteResponse> getInvite(
            @RequestParam(required = false, name = "status") InviteStatus status,
            @RequestParam(required = false, name = "household_id") Household household
    ) {
        return inviteService.getInvites(status, household);
    }

    @PostMapping
    public InviteResponse postInvite(@RequestBody CreateInviteRequest request) {
        return inviteService.createInvite(request);
    }

    @PatchMapping("/{id}")
    public InviteResponse patchInvite(
            @PathVariable UUID id,
            @RequestBody UpdateInviteRequest request
    ) {
        return inviteService.updateInvite(id, request);
    }

    @PatchMapping("/status/{id}")
    public InviteResponse patchInviteStatus(
            @PathVariable UUID id,
            @RequestBody UpdateInviteStatusRequest request
    ) {
        return inviteService.updateInviteStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteInvite(@PathVariable UUID id) { inviteService.deleteInvite(id); }
}
