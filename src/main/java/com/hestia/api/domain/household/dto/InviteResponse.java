package com.hestia.api.domain.household.dto;

import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.enums.InviteAge;
import com.hestia.api.domain.household.enums.InviteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InviteResponse {

    private UUID id;
    private String name;
    private InviteAge ageGroup;
    private InviteStatus status;
    private LocalDateTime createdAt;
}
