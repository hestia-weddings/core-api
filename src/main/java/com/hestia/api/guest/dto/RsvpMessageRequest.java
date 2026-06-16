package com.hestia.api.guest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsvpMessageRequest {

    @NotNull
    private UUID inviteId;

    private String sender;

    @NotBlank
    private String message;
}
