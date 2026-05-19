package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GuestResponse(UUID id, String name, GuestAge ageGroup, GuestStatus status, LocalDateTime createdAt) {}
