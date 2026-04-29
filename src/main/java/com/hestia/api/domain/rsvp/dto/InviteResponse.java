package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.InviteAge;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
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
