package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.CannotDeleteConfirmedGuestException;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.CreateGuestRequest;
import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.dto.UpdateGuestRequest;
import com.hestia.api.domain.rsvp.dto.UpdateGuestStatusRequest;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.repository.InviteRepository;
import com.hestia.api.domain.rsvp.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;
    private final InviteRepository inviteRepository;

    public GuestResponse toResponse(Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .ageGroup(guest.getAgeGroup())
                .status(guest.getStatus())
                .createdAt(guest.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public PageResponse<GuestResponse> getGuests(Pageable pageable, GuestStatus status, Invite invite) {
        Page<GuestResponse> guest;

        if (invite != null)
            guest = guestRepository.findByInviteAndIsActiveTrue(pageable, invite)
                    .map(this::toResponse);

        else if (status != null)
            guest = guestRepository.findByStatusAndIsActiveTrue(pageable, status)
                    .map(this::toResponse);

        else
            guest = guestRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(guest);
    }

    private Guest getGuest(UUID id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
    }

    public GuestResponse createGuest(CreateGuestRequest request) {
        Invite invite = inviteRepository.findById(request.getInviteId())
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));

        Guest guest = Guest.builder()
                .name(request.getName())
                .ageGroup(request.getAgeGroup())
                .status(GuestStatus.PENDING)
                .invite(invite)
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        return this.toResponse(guestRepository.save(guest));
    }

    public GuestResponse updateGuest(UUID id, UpdateGuestRequest request) {
        Guest guest = getGuest(id);

        if (request.getName() != null)
            guest.setName(request.getName());
        if (request.getAgeGroup() != null)
            guest.setAgeGroup(request.getAgeGroup());

        return this.toResponse(guestRepository.save(guest));
    }

    public GuestResponse updateGuestStatus(UUID id, UpdateGuestStatusRequest request) {
        Guest guest = getGuest(id);

        guest.setStatus(request.getStatus());

        return this.toResponse(guestRepository.save(guest));
    }

    public void deleteGuest(UUID id) {
        Guest guest = getGuest(id);

        if (guest.getStatus() == GuestStatus.CONFIRMED)
            throw new CannotDeleteConfirmedGuestException();

        if (!guest.getIsActive())
            throw new ResourceNotFoundException("Guest not found");

        guest.setIsActive(false);

        guestRepository.save(guest);
    }
}
