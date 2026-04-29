package com.hestia.api.domain.household.dto;

import com.hestia.api.domain.household.enums.InviteStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInviteStatusRequest {

    private InviteStatus status;
}
