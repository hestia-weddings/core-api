package com.hestia.api.domain.wedding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateWeddingRequest {

    @NotBlank
    private String coupleName;

    private LocalDateTime date;

    private String inviteMessage;

    private String giftMessage;

    @NotBlank
    private String slug;
}
