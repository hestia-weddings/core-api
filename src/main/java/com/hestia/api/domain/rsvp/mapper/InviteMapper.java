package com.hestia.api.domain.rsvp.mapper;

import com.hestia.api.domain.rsvp.dto.InviteResponse;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.entity.Invite;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InviteMapper {

    private final GuestMapper guestMapper;

    public InviteResponse toResponse(Invite invite) {
        return InviteResponse.builder()
                .id(invite.getId())
                .name(invite.getName())
                .phone(invite.getPhone())
                .createdAt(invite.getCreatedAt())
                .guests(invite.getGuests().stream()
                        .filter(Guest::getIsActive)
                        .map(guestMapper::toResponse)
                        .toList())
                .build();
    }
}
