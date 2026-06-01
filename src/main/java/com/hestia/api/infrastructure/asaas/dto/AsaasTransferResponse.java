package com.hestia.api.infrastructure.asaas.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasTransferResponse(String id, String status, BigDecimal value) {}
