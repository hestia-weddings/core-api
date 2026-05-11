package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateGuestRequest {

    private String name;
    private GuestAge ageGroup;
    private UUID inviteId;
}
