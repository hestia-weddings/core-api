package com.hestia.api.domain.rsvp.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record InviteResponse(
        UUID id,
        String name,
        String phone,
        LocalDateTime createdAt,
        List<GuestResponse> guests
) {}
