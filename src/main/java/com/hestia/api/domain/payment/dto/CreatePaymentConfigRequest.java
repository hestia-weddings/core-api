package com.hestia.api.domain.payment.dto;

import com.hestia.api.domain.payment.enums.PaymentEnvironment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentConfigRequest {

    @NotBlank
    private String apiKey;

    @NotNull
    private PaymentEnvironment environment;

    @NotBlank
    private String webhookToken;
}
