package com.hestia.api.domain.registry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGiftRequest {

    @NotBlank
    private String description;

    private String picture;

    @Positive
    private Integer price;

    @Positive
    private Integer stock;
}
