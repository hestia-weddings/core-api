package com.hestia.api.domain.wedding.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWeddingRequest {

    @NotBlank
    private String coupleName;

    private LocalDate date;

    private String inviteMessage;

    private String giftMessage;

    @NotBlank
    private String slug;
}
