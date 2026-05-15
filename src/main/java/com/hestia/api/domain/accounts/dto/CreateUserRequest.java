package com.hestia.api.domain.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private UUID weddingId;
}
