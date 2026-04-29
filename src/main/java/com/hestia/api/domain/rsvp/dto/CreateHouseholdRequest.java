package com.hestia.api.domain.rsvp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateHouseholdRequest {

    private String name;
    private String phone;
    private List<CreateInviteRequest> invites;
}
