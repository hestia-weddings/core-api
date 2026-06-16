package com.hestia.api.domain.wedding.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record WeddingResponse(
        UUID id,
        String coupleName,
        LocalDate date,
        String picture,
        String inviteMessage,
        String giftMessage,
        String slug,
        LocalDateTime createdAt) {}
