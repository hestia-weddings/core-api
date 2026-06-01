package com.hestia.api.domain.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    @NotNull
    @Min(1)
    private Integer amount;
}
