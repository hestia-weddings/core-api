package com.hestia.api.domain.household.service;

import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.enums.InviteStatus;
import com.hestia.api.domain.household.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

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

    public void deleteInvite(UUID id) {
        Invite invite = getInviteById(id);

        if (invite.getStatus() == InviteStatus.CONFIRMED)
            throw new RuntimeException("Cannot delete invites already confirmed!");

        invite.setIsActive(false);
        invite.setUpdatedAt(LocalDateTime.now());

        inviteRepository.save(invite);
    }
}
