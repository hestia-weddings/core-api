package com.hestia.api.domain.household.service;

import com.hestia.api.domain.household.dto.HouseholdResponse;
import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.repository.HouseholdRepository;
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

    public void deleteHousehold(UUID id) {
        Household household = getHouseholdById(id);

        household.setIsActive(false);
        household.setUpdatedAt(LocalDateTime.now());

        householdRepository.save(household);
    }
}
