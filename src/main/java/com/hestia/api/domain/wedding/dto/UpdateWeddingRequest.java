package com.hestia.api.domain.wedding.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWeddingRequest {

    private String coupleName;

    private LocalDate date;

    private String inviteMessage;

    private String giftMessage;
}
