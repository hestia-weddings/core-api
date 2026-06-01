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

    @NotBlank
    private String cpfCnpj;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String addressNumber;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String province;
}
