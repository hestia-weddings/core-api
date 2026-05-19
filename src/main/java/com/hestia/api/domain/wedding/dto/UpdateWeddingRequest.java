package com.hestia.api.domain.wedding.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWeddingRequest {

    private String coupleName;

    private LocalDateTime date;

    private String inviteMessage;

    private String giftMessage;
}
