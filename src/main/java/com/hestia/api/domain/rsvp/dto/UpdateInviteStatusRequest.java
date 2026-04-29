package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.InviteStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInviteStatusRequest {

    private InviteStatus status;
}
