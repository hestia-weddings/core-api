package com.hestia.api.domain.rsvp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInviteRequest {

    @NotBlank
    private String name;

    private String phone;

    private List<@Valid InlineGuestRequest> guests;
}
