package com.hestia.api.domain.message.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMessageRequest {

    @NotNull
    private Boolean isFavorite;
}
