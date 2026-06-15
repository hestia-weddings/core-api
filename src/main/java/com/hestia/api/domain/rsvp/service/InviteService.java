package com.hestia.api.domain.rsvp.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.CannotDeleteInviteWithConfirmedGuestsException;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.rsvp.dto.*;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.mapper.InviteMapper;
import com.hestia.api.domain.rsvp.repository.GuestRepository;
import com.hestia.api.domain.rsvp.repository.InviteRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;

import jakarta.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteService {

    private final InviteRepository inviteRepository;
    private final GuestRepository guestRepository;
    private final InviteMapper inviteMapper;
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<InviteResponse> getInvites(
            @Nullable UUID weddingId, @Nullable GuestStatus status, Pageable pageable) {
        Page<Invite> page;

        if (weddingId != null && status != null)
            page = inviteRepository.findByWeddingIdAndGuestStatus(weddingId, status.name(), pageable);
        else if (weddingId != null) page = inviteRepository.findByWeddingIdAndIsActiveTrue(weddingId, pageable);
        else page = inviteRepository.findAll(pageable);

        if (status != null) {
            return PageMapper.toResponse(page.map(invite -> inviteMapper.toResponse(invite, status)));
        }
        return PageMapper.toResponse(page.map(inviteMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public InviteResponse getInviteById(@Nullable UUID weddingId, UUID id) {
        return inviteMapper.toResponse(getInvite(weddingId, id));
    }

    private Invite getInvite(@Nullable UUID weddingId, UUID id) {
        if (weddingId == null)
            return inviteRepository
                    .findByIdAndIsActiveTrue(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));
        return inviteRepository
                .findByIdAndWeddingIdAndIsActiveTrue(id, weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));
    }

    public InviteResponse createInvite(UUID weddingId, CreateInviteRequest request) {
        Wedding wedding = weddingRepository
                .findByIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        Invite invite = Invite.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .wedding(wedding)
                .build();

        Invite savedInvite = inviteRepository.save(invite);

        if (request.getGuests() != null && !request.getGuests().isEmpty()) {
            List<Guest> guests = request.getGuests().stream()
                    .map(guestRequest -> (Guest) Guest.builder()
                            .name(guestRequest.getName())
                            .ageGroup(guestRequest.getAgeGroup())
                            .status(GuestStatus.PENDING)
                            .invite(savedInvite)
                            .wedding(wedding)
                            .build())
                    .toList();

            guestRepository.saveAll(guests);
            savedInvite.setGuests(guests);
        }

        return inviteMapper.toResponse(savedInvite);
    }

    public InviteResponse updateInvite(@Nullable UUID weddingId, UUID id, UpdateInviteRequest request) {
        Invite invite = getInvite(weddingId, id);

        if (request.getName() != null) invite.setName(request.getName());
        if (request.getPhone() != null) invite.setPhone(request.getPhone());

        return inviteMapper.toResponse(inviteRepository.save(invite));
    }

    public void deleteInvite(@Nullable UUID weddingId, UUID id) {
        Invite invite = getInvite(weddingId, id);

        boolean hasConfirmedGuests = invite.getGuests().stream()
                .filter(Guest::getIsActive)
                .anyMatch(guest -> guest.getStatus() == GuestStatus.CONFIRMED);

        if (hasConfirmedGuests) throw new CannotDeleteInviteWithConfirmedGuestsException();

        List<Guest> activeGuests =
                invite.getGuests().stream().filter(Guest::getIsActive).toList();
        activeGuests.forEach(guest -> guest.setIsActive(false));
        guestRepository.saveAll(activeGuests);

        invite.setIsActive(false);
        inviteRepository.save(invite);
    }

    @Transactional(readOnly = true)
    public InviteResponse searchInvite(UUID weddingId, SearchInviteRequest request) {
        Invite invite = inviteRepository
                .findByNameIgnoreCaseAndWeddingIdAndIsActiveTrue(request.getName(), weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));

        return inviteMapper.toResponse(invite);
    }
}
