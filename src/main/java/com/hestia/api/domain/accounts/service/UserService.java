package com.hestia.api.domain.accounts.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.accounts.dto.CreateUserRequest;
import com.hestia.api.domain.accounts.dto.UpdateUserRequest;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.entity.User;
import com.hestia.api.domain.accounts.enums.UserRole;
import com.hestia.api.domain.accounts.mapper.UserMapper;
import com.hestia.api.domain.accounts.repository.UserRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsers(Pageable pageable) {
        Page<UserResponse> user = userRepository.findByIsActiveTrue(pageable).map(userMapper::toResponse);

        return PageMapper.toResponse(user);
    }

    private User getUser(UUID id) {
        return userRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse createUser(CreateUserRequest request) {
        Wedding wedding = weddingRepository
                .findByIdAndIsActiveTrue(request.getWeddingId())
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(UserRole.COUPLE)
                .authUserId(UUID.fromString("187b00ff-fa02-4182-94bb-02184f7bde48"))
                .wedding(wedding)
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = getUser(id);

        user.setName(request.getName());

        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = getUser(id);
        user.setIsActive(false);
        userRepository.save(user);
    }
}
