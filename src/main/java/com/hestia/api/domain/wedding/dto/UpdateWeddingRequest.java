package com.hestia.api.domain.wedding.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateWeddingRequest {

    private String coupleName;

    private LocalDateTime date;

    private String inviteMessage;

    private String giftMessage;
}
