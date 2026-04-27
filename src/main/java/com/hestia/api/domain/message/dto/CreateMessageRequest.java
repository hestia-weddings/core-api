package com.hestia.api.domain.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMessageRequest {

    private String sender;
    private String message;
}
