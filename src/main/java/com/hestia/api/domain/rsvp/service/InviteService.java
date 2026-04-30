package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.CreateInviteRequest;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.UpdateInviteRequest;
import com.hestia.api.domain.rsvp.dto.UpdateInviteStatusRequest;
import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
import com.hestia.api.domain.rsvp.repository.HouseholdRepository;
import com.hestia.api.domain.rsvp.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final HouseholdRepository householdRepository;

    public InviteResponse toResponse(Invite invite) {
        return InviteResponse.builder()
                .id(invite.getId())
                .name(invite.getName())
                .ageGroup(invite.getAgeGroup())
                .status(invite.getStatus())
                .createdAt(invite.getCreatedAt())
                .build();
    }

    public PageResponse<InviteResponse> getInvites(Pageable pageable, InviteStatus status, Household household) {
        Page<InviteResponse> invite;

        if (household != null)
            invite = inviteRepository.findByHouseholdAndIsActiveTrue(pageable, household)
                    .map(this::toResponse);

        else if (status != null)
            invite = inviteRepository.findByStatusAndIsActiveTrue(pageable, status)
                    .map(this::toResponse);

        else
            invite = inviteRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(invite);
    }

    private Invite getInvite(UUID id) {
        return inviteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));
    }

    public InviteResponse createInvite(CreateInviteRequest request) {
        Household household = householdRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new ResourceNotFoundException("Household not found"));

        Invite invite = Invite.builder()
                .name(request.getName())
                .ageGroup(request.getAgeGroup())
                .status(InviteStatus.PENDING)
                .household(household)
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        return this.toResponse(inviteRepository.save(invite));
    }

    public InviteResponse updateInvite(UUID id, UpdateInviteRequest request) {
        Invite invite = getInvite(id);

        if (request.getName() != null)
            invite.setName(request.getName());
        if (request.getAgeGroup() != null)
            invite.setAgeGroup(request.getAgeGroup());

        return this.toResponse(inviteRepository.save(invite));
    }

    public InviteResponse updateInviteStatus(UUID id, UpdateInviteStatusRequest request) {
        Invite invite = getInvite(id);

        invite.setStatus(request.getStatus());

        return this.toResponse(inviteRepository.save(invite));
    }

    public void deleteInvite(UUID id) {
        Invite invite = getInvite(id);

        if (invite.getStatus() == InviteStatus.CONFIRMED)
            throw new RuntimeException("Cannot delete invites already confirmed!");

        invite.setIsActive(false);

        inviteRepository.save(invite);
    }
}
