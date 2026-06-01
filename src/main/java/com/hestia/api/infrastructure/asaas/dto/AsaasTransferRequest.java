package com.hestia.api.infrastructure.asaas.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AsaasTransferRequest(
        BigDecimal value,
        @JsonProperty("operationType") String operationType,
        @JsonProperty("pixAddressKey") String pixAddressKey) {}
