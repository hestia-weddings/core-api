package com.hestia.api.infrastructure.asaas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsaasCheckoutResponse {

    private String id;
    private String link;
    private String status;
}
