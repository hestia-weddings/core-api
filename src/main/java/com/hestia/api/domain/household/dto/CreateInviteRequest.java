package com.hestia.api.domain.household.dto;

import com.hestia.api.domain.household.enums.InviteAge;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateInviteRequest {

    private String name;
    private InviteAge ageGroup;
    private UUID householdId;
}
