package com.hestia.api.domain.message.mapper;

import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.entity.Message;

import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(message.getSender())
                .message(message.getMessage())
                .isFavorite(message.getIsFavorite())
                .isNew(message.getIsNew())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
