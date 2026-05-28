package com.hestia.api.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWalletRequest {

    @NotBlank
    private String pixKey;
}
