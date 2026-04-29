package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.InviteAge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInviteRequest {

    private String name;
    private InviteAge ageGroup;
}
