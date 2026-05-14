package com.hestia.api.domain.rsvp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateInviteRequest {

    @NotBlank
    private String name;

    private String phone;

    private List<@Valid CreateGuestRequest> guests;
}
