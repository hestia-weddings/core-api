package com.hestia.api.guest.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {

    @NotBlank
    private String guestName;

    @NotBlank
    private String guestEmail;

    private String sender;

    private String message;
}
