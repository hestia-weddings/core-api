package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.CannotDeleteHouseholdWithConfirmedGuestsException;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.CreateHouseholdRequest;
import com.hestia.api.domain.rsvp.dto.HouseholdResponse;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.UpdateHouseholdRequest;
import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.repository.HouseholdRepository;
import com.hestia.api.domain.rsvp.repository.GuestRepository;
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
    private final GuestRepository guestRepository;

    private HouseholdResponse toResponse(Household household) {
        List<GuestResponse> guests = household.getGuests()
                .stream()
                .filter(Guest::getIsActive)
                .map((guest) -> GuestResponse.builder()
                        .id(guest.getId())
                        .name(guest.getName())
                        .ageGroup(guest.getAgeGroup())
                        .status(guest.getStatus())
                        .createdAt(guest.getCreatedAt())
                        .build())
                .toList();
        
        return HouseholdResponse.builder()
                .id(household.getId())
                .name(household.getName())
                .phone(household.getPhone())
                .createdAt(household.getCreatedAt())
                .guests(guests)
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

        if (request.getGuests() != null && !request.getGuests().isEmpty()) {
            List<Guest> guests = request.getGuests().stream()
                    .map(guestRequest -> Guest.builder()
                            .name(guestRequest.getName())
                            .ageGroup(guestRequest.getAgeGroup())
                            .status(GuestStatus.PENDING)
                            .household(savedHousehold)
                            .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                            .build())
                    .toList();

            guestRepository.saveAll(guests);
            savedHousehold.setGuests(guests);
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

        boolean hasConfirmedGuests = household.getGuests().stream()
                .filter(Guest::getIsActive)
                .anyMatch(guest -> guest.getStatus() == GuestStatus.CONFIRMED);

        if (hasConfirmedGuests)
            throw new CannotDeleteHouseholdWithConfirmedGuestsException();

        household.setIsActive(false);

        household.getGuests().stream()
                .filter(Guest::getIsActive)
                .forEach(guest -> guest.setIsActive(false));

        householdRepository.save(household);
    }
}
