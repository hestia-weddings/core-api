package com.hestia.api.domain.registry.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGiftRequest {

    @Size(min = 1)
    private String description;

    private String picture;

    @Positive
    private Integer price;

    @Positive
    private Integer stock;
}
