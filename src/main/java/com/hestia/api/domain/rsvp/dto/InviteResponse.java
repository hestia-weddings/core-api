package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.message.dto.MessageResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record InviteResponse(
        UUID id,
        String name,
        String phone,
        LocalDateTime createdAt,
        List<GuestResponse> guests,
        MessageResponse message) {}
