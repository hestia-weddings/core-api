package com.hestia.api.domain.rsvp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateInviteRequest {

    @NotBlank
    private String name;

    private String phone;

    private List<@Valid CreateGuestRequest> guests;

    @NotBlank
    private UUID weddingId;
}
