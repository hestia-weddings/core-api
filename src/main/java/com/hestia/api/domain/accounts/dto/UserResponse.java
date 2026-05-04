package com.hestia.api.domain.accounts.dto;

import com.hestia.api.domain.accounts.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private UUID authUserId;
}
