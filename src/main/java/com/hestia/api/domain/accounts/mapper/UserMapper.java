package com.hestia.api.domain.accounts.mapper;

import com.hestia.api.domain.accounts.dto.CoupleAccountResponse;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .authUserId(user.getAuthUserId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public CoupleAccountResponse toCoupleResponse(User user) {
        return CoupleAccountResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
