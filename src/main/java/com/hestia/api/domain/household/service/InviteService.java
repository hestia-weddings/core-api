package com.hestia.api.domain.household.service;

import com.hestia.api.domain.household.dto.CreateInviteRequest;
import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.dto.UpdateInviteRequest;
import com.hestia.api.domain.household.dto.UpdateInviteStatusRequest;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.enums.InviteAge;
import com.hestia.api.domain.household.enums.InviteStatus;
import com.hestia.api.domain.household.repository.HouseholdRepository;
import com.hestia.api.domain.household.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

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

    public List<InviteResponse> getInvites(InviteStatus status, Household household) {
        List<Invite> invites;

        if (household != null)
            invites = inviteRepository.findByHouseholdAndIsActiveTrue(household);
        else if (status != null)
            invites = inviteRepository.findByStatusAndIsActiveTrue(status);
        else
            invites = inviteRepository.findByIsActiveTrue();

        return invites.stream()
                .map(this::toResponse)
                .toList();
    }

    private Invite getInviteById(UUID id) {
        return inviteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invite not found"));
    }

    public InviteResponse createInvite(CreateInviteRequest request) {
        Household household = householdRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new RuntimeException("Household not found"));

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
        Invite invite = getInviteById(id);

        if (request.getName() != null)
            invite.setName(request.getName());
        if (request.getAgeGroup() != null)
            invite.setAgeGroup(request.getAgeGroup());

        return this.toResponse(inviteRepository.save(invite));
    }

    public InviteResponse updateInviteStatus(UUID id, UpdateInviteStatusRequest request) {
        Invite invite = getInviteById(id);

        invite.setStatus(request.getStatus());

        return this.toResponse(inviteRepository.save(invite));
    }

    public void deleteInvite(UUID id) {
        Invite invite = getInviteById(id);

        if (invite.getStatus() == InviteStatus.CONFIRMED)
            throw new RuntimeException("Cannot delete invites already confirmed!");

        invite.setIsActive(false);
        invite.setUpdatedAt(LocalDateTime.now());

        inviteRepository.save(invite);
    }
}
