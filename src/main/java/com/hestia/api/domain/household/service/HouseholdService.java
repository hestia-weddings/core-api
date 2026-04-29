package com.hestia.api.domain.household.service;

import com.hestia.api.domain.household.dto.HouseholdResponse;
import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.enums.InviteStatus;
import com.hestia.api.domain.household.repository.HouseholdRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;

    private HouseholdResponse toResponse(Household household) {
        List<InviteResponse> invites = household.getInvites()
                .stream()
                .filter(Invite::getIsActive)
                .map((invite) -> InviteResponse.builder()
                        .id(invite.getId())
                        .name(invite.getName())
                        .ageGroup(invite.getAgeGroup())
                        .status(invite.getStatus())
                        .createdAt(invite.getCreatedAt())
                        .build())
                .toList();
        
        return HouseholdResponse.builder()
                .id(household.getId())
                .name(household.getName())
                .phone(household.getPhone())
                .createdAt(household.getCreatedAt())
                .invites(invites)
                .build();
    }

    public List<HouseholdResponse> getHouseholds() {
        return householdRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Household getHouseholdById(UUID id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));
    }

    @Transactional
    public void deleteHousehold(UUID id) {
        Household household = getHouseholdById(id);
        LocalDateTime now = LocalDateTime.now();

        boolean hasConfirmedInvites = household.getInvites().stream()
                .filter(Invite::getIsActive)
                .anyMatch(invite -> invite.getStatus() == InviteStatus.CONFIRMED);

        if (hasConfirmedInvites)
            throw new RuntimeException("Cannot delete household with confirmed invites!");

        household.setIsActive(false);
        household.setUpdatedAt(now);

        household.getInvites().stream()
                .filter(Invite::getIsActive)
                .forEach(invite -> {
                    invite.setIsActive(false);
                    invite.setUpdatedAt(now);
                });

        householdRepository.save(household);
    }
}
