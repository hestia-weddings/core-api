package com.hestia.api.domain.registry.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGiftRequest {

    private String description;

    private String picture;

    @Positive
    private Integer price;

    @Positive
    private Integer stock;
}
