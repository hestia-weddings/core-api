package com.hestia.api.infrastructure.asaas.dto;

import java.math.BigDecimal;

public record AsaasTransferRequest(BigDecimal value, String operationType, String pixAddressKey) {}
