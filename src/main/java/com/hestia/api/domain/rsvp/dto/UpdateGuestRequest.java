package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuestRequest {

    private String name;
    private GuestAge ageGroup;
}
