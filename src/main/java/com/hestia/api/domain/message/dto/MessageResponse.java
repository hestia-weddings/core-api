package com.hestia.api.domain.message.dto;

import com.hestia.api.domain.message.enums.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record MessageResponse(
        UUID id,
        MessageType type,
        String sender,
        String message,
        Boolean isFavorite,
        Boolean isNew,
        LocalDateTime createdAt) {}
