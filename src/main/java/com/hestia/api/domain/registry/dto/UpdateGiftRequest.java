package com.hestia.api.domain.registry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGiftRequest {

    private String description;
    private String picture;
    private Integer price;
    private Integer stock;
}
