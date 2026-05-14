package com.hestia.api.domain.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMessageRequest {

    @NotBlank
    private String sender;

    @NotBlank
    private String message;
}
