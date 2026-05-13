package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GuestResponse(
        UUID id,
        String name,
        GuestAge ageGroup,
        GuestStatus status,
        LocalDateTime createdAt
) {}
