package com.hestia.api.domain.rsvp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInviteRequest {

    @NotBlank
    private String name;
}
