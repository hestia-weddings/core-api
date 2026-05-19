package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGuestRequest {

    @NotBlank
    private String name;

    @NotNull
    private GuestAge ageGroup;

    @NotNull
    private UUID inviteId;
}
