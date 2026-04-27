package com.hestia.api.domain.message.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MessageResponse {

    private UUID id;
    private String sender;
    private String message;
    private Boolean isFavorite;
    private Boolean isNew;
    private LocalDateTime createdAt;
}
