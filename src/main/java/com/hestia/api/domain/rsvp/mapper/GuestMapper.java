package com.hestia.api.domain.rsvp.mapper;

import com.hestia.api.domain.rsvp.dto.GuestResponse;
import com.hestia.api.domain.rsvp.entity.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public GuestResponse toResponse(Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .ageGroup(guest.getAgeGroup())
                .status(guest.getStatus())
                .createdAt(guest.getCreatedAt())
                .build();
    }
}
