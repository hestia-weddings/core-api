package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuestStatusRequest {

    private GuestStatus status;
}
