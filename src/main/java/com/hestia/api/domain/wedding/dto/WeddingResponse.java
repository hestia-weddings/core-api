package com.hestia.api.domain.wedding.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record WeddingResponse(
        UUID id,
        String coupleName,
        LocalDateTime date,
        String inviteMessage,
        String giftMessage,
        String slug,
        LocalDateTime createdAt) {}
