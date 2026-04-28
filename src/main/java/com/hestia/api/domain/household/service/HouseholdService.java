package com.hestia.api.domain.household.service;

import com.hestia.api.domain.household.dto.HouseholdResponse;
import com.hestia.api.domain.household.dto.InviteResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.entity.Invite;
import com.hestia.api.domain.household.repository.HouseholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
