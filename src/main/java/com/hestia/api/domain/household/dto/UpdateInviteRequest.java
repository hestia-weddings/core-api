package com.hestia.api.domain.household.dto;

import com.hestia.api.domain.household.enums.InviteAge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInviteRequest {

    private String name;
    private InviteAge ageGroup;
}
