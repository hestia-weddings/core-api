package com.hestia.api.domain.accounts.dto;

import com.hestia.api.domain.accounts.enums.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        UUID authUserId
) {}
