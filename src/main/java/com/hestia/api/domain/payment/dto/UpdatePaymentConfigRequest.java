package com.hestia.api.domain.payment.dto;

import com.hestia.api.domain.payment.enums.PaymentEnvironment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentConfigRequest {

    private String apiKey;

    private PaymentEnvironment environment;
}
