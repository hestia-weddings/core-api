package com.hestia.api.domain.household.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class HouseholdResponse {

    private UUID id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private List<InviteResponse> invites;
}
