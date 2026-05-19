package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuestStatusRequest {

    @NotNull
    private GuestStatus status;
}
