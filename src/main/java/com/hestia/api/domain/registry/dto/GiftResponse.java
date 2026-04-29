package com.hestia.api.domain.registry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GiftResponse {

    private UUID id;
    private String description;
    private String picture;
    private Integer price;
    private Integer stock;
    private LocalDateTime createdAt;
}
