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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsers(UUID weddingId, Pageable pageable) {
        Page<UserResponse> user = userRepository.findByWeddingIdAndIsActiveTrue(weddingId, pageable)
                .map(userMapper::toResponse);

        return PageMapper.toResponse(user);
    }

    private User getUser(UUID weddingId, UUID id) {
        return userRepository.findByIdAndWeddingIdAndIsActiveTrue(id, weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse createUser(UUID weddingId, CreateUserRequest request) {
        Wedding wedding = weddingRepository.findByIdAndIsActiveTrue(weddingId)
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

    public UserResponse updateUser(UUID weddingId, UUID id, UpdateUserRequest request) {
        User user = getUser(weddingId, id);

        user.setName(request.getName());

        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID weddingId, UUID id) {
        User user = getUser(weddingId, id);
        user.setIsActive(false);
        userRepository.save(user);
    }
}
