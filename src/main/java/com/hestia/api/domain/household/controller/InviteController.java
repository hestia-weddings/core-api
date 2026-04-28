package com.hestia.api.domain.household.controller;

import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.enums.InviteStatus;
import com.hestia.api.domain.household.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
