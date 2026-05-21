package com.hestia.api.domain.rsvp.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInviteRequest {

    @NotBlank
    private String name;

    private String phone;
}
