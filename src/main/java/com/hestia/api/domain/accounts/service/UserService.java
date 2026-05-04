package com.hestia.api.domain.accounts.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.entity.User;
import com.hestia.api.domain.accounts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public PageResponse<UserResponse> getUsers(Pageable pageable) {
        Page<UserResponse> user;

        user = userRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(user);
    }
}
