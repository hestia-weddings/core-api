package com.hestia.api.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status, String error, String message, Map<String, String> fields, LocalDateTime timestamp) {}
