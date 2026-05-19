package com.hestia.api.domain.rsvp.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record InviteResponse(UUID id, String name, String phone, LocalDateTime createdAt, List<GuestResponse> guests) {}
