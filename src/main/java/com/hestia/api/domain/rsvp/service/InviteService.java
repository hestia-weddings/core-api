package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.CannotDeleteInviteWithConfirmedGuestsException;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.*;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteService {

    private final InviteRepository inviteRepository;
    private final GuestRepository guestRepository;

    private InviteResponse toResponse(Invite invite) {
        List<GuestResponse> guests = invite.getGuests()
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
        
        return InviteResponse.builder()
                .id(invite.getId())
                .name(invite.getName())
                .phone(invite.getPhone())
                .createdAt(invite.getCreatedAt())
                .guests(guests)
                .build();
    }

    @Transactional(readOnly = true)
    public PageResponse<InviteResponse> getInvites(Pageable pageable) {
        Page<InviteResponse> invite = inviteRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(invite);
    }

    private Invite getInvite(UUID id) {
        return inviteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));
    }

    public InviteResponse createInvite(CreateInviteRequest request) {
        Invite invite = Invite.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        Invite savedInvite = inviteRepository.save(invite);

        if (request.getGuests() != null && !request.getGuests().isEmpty()) {
            List<Guest> guests = request.getGuests().stream()
                    .map(guestRequest -> Guest.builder()
                            .name(guestRequest.getName())
                            .ageGroup(guestRequest.getAgeGroup())
                            .status(GuestStatus.PENDING)
                            .invite(savedInvite)
                            .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                            .build())
                    .toList();

            guestRepository.saveAll(guests);
            savedInvite.setGuests(guests);
        }

        return this.toResponse(invite);
    }

    public InviteResponse updateInvite(UUID id, UpdateInviteRequest request) {
        Invite invite = getInvite(id);

        if (request.getName() != null)
            invite.setName(request.getName());
        if (request.getPhone() != null)
            invite.setPhone(request.getPhone());

        return this.toResponse(inviteRepository.save(invite));
    }

    public void deleteInvite(UUID id) {
        Invite invite = getInvite(id);

        if (!invite.getIsActive())
            throw new ResourceNotFoundException("Invite not found");

        boolean hasConfirmedGuests = invite.getGuests().stream()
                .filter(Guest::getIsActive)
                .anyMatch(guest -> guest.getStatus() == GuestStatus.CONFIRMED);

        if (hasConfirmedGuests)
            throw new CannotDeleteInviteWithConfirmedGuestsException();

        invite.setIsActive(false);

        invite.getGuests().stream()
                .filter(Guest::getIsActive)
                .forEach(guest -> guest.setIsActive(false));

        inviteRepository.save(invite);
    }

    @Transactional(readOnly = true)
    public InviteResponse searchInvite(SearchInviteRequest request) {
        Invite invite = inviteRepository
                .findByNameIgnoreCaseAndIsActiveTrue(request.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invite not found"));

        return this.toResponse(invite);
    }
}
