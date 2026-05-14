package com.hestia.api.domain.registry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateGiftRequest {

    @NotBlank
    private String description;

    private String picture;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer stock;

    @NotBlank
    private UUID weddingId;
}
