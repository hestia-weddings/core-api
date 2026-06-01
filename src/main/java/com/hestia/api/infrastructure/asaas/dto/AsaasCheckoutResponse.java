package com.hestia.api.infrastructure.asaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsaasCheckoutResponse {

    private String id;
    private String link;
    private String status;
}
