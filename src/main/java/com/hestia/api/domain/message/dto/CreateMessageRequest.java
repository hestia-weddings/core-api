package com.hestia.api.domain.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateMessageRequest {

    @NotBlank
    private String sender;

    @NotBlank
    private String message;

    @NotBlank
    private UUID weddingId;
}
