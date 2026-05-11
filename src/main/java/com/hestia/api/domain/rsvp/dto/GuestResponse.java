package com.hestia.api.domain.rsvp.dto;

import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GuestResponse {

    private UUID id;
    private String name;
    private GuestAge ageGroup;
    private GuestStatus status;
    private LocalDateTime createdAt;
}
