package com.hestia.api.infrastructure.asaas.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AsaasCheckoutRequest {

    private List<String> billingTypes;
    private List<String> chargeTypes;
    private Integer minutesToExpire;
    private String externalReference;
    private Callback callback;
    private List<Item> items;
    private CustomerData customerData;

    @Getter
    @Setter
    @Builder
    public static class Callback {
        private String successUrl;
        private String cancelUrl;
        private String expiredUrl;
    }

    @Getter
    @Setter
    @Builder
    public static class Item {
        private String name;
        private String description;
        private Integer quantity;
        private Double value;
    }

    @Getter
    @Setter
    @Builder
    public static class CustomerData {
        private String name;
        private String email;
    }
}
