package com.hestia.api.domain.wedding.mapper;

import com.hestia.api.domain.wedding.dto.WeddingResponse;
import com.hestia.api.domain.wedding.entity.Wedding;

import org.springframework.stereotype.Component;

@Component
public class WeddingMapper {

    public WeddingResponse toResponse(Wedding wedding) {
        return WeddingResponse.builder()
                .id(wedding.getId())
                .coupleName(wedding.getCoupleName())
                .date(wedding.getDate())
                .picture(wedding.getPicture())
                .inviteMessage(wedding.getInviteMessage())
                .giftMessage(wedding.getGiftMessage())
                .slug(wedding.getSlug())
                .createdAt(wedding.getCreatedAt())
                .build();
    }
}
