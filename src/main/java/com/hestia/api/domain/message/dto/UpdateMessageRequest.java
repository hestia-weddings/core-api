package com.hestia.api.domain.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMessageRequest {

    private Boolean isNew;
    private Boolean isFavorite;
}
