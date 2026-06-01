package com.hestia.api.infrastructure.asaas.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AsaasCheckoutRequest {

    @com.fasterxml.jackson.annotation.JsonProperty("billingTypes")
    private List<String> billingTypes;

    @com.fasterxml.jackson.annotation.JsonProperty("chargeTypes")
    private List<String> chargeTypes;

    @com.fasterxml.jackson.annotation.JsonProperty("minutesToExpire")
    private Integer minutesToExpire;

    @com.fasterxml.jackson.annotation.JsonProperty("externalReference")
    private String externalReference;

    private Callback callback;
    private List<Item> items;

    @com.fasterxml.jackson.annotation.JsonProperty("customerData")
    private CustomerData customerData;

    @Getter
    @Setter
    @Builder
    public static class Callback {

        @com.fasterxml.jackson.annotation.JsonProperty("successUrl")
        private String successUrl;

        @com.fasterxml.jackson.annotation.JsonProperty("cancelUrl")
        private String cancelUrl;

        @com.fasterxml.jackson.annotation.JsonProperty("expiredUrl")
        private String expiredUrl;
    }

    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
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

        @com.fasterxml.jackson.annotation.JsonProperty("cpfCnpj")
        private String cpfCnpj;

        @com.fasterxml.jackson.annotation.JsonProperty("phone")
        private String phoneNumber;

        private String address;

        @com.fasterxml.jackson.annotation.JsonProperty("addressNumber")
        private String addressNumber;

        @com.fasterxml.jackson.annotation.JsonProperty("postalCode")
        private String postalCode;

        private String province;
    }
}
