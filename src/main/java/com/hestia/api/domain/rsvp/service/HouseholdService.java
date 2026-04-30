package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.CreateHouseholdRequest;
import com.hestia.api.domain.rsvp.dto.HouseholdResponse;
import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.dto.UpdateHouseholdRequest;
import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
import com.hestia.api.domain.rsvp.repository.HouseholdRepository;
import com.hestia.api.domain.rsvp.repository.InviteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final InviteRepository inviteRepository;

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

    public PageResponse<HouseholdResponse> getHouseholds(Pageable pageable) {
        Page<HouseholdResponse> household = householdRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(household);
    }

    private Household getHousehold(UUID id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found"));
    }

    @Transactional
    public HouseholdResponse createHousehold(CreateHouseholdRequest request) {
        Household household = Household.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        Household savedHousehold = householdRepository.save(household);

        if (request.getInvites() != null && !request.getInvites().isEmpty()) {
            List<Invite> invites = request.getInvites().stream()
                    .map(invitedRequest -> Invite.builder()
                            .name(invitedRequest.getName())
                            .ageGroup(invitedRequest.getAgeGroup())
                            .status(InviteStatus.PENDING)
                            .household(savedHousehold)
                            .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                            .build())
                    .toList();

            inviteRepository.saveAll(invites);
            savedHousehold.setInvites(invites);
        }

        return this.toResponse(household);
    }

    public HouseholdResponse updateHousehold(UUID id, UpdateHouseholdRequest request) {
        Household household = getHousehold(id);

        if (request.getName() != null)
            household.setName(request.getName());
        if (request.getPhone() != null)
            household.setPhone(request.getPhone());

        return this.toResponse(householdRepository.save(household));
    }

    @Transactional
    public void deleteHousehold(UUID id) {
        Household household = getHousehold(id);

        boolean hasConfirmedInvites = household.getInvites().stream()
                .filter(Invite::getIsActive)
                .anyMatch(invite -> invite.getStatus() == InviteStatus.CONFIRMED);

        if (hasConfirmedInvites)
            throw new RuntimeException("Cannot delete household with confirmed invites!");

        household.setIsActive(false);

        household.getInvites().stream()
                .filter(Invite::getIsActive)
                .forEach(invite -> invite.setIsActive(false));

        householdRepository.save(household);
    }
}
