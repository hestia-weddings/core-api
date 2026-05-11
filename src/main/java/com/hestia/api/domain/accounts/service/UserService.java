package com.hestia.api.domain.accounts.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.accounts.dto.CreateUserRequest;
import com.hestia.api.domain.accounts.dto.UpdateUserRequest;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.entity.User;
import com.hestia.api.domain.accounts.enums.UserRole;
import com.hestia.api.domain.accounts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    private User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(UserRole.COUPLE)
                .authUserId(UUID.fromString("187b00ff-fa02-4182-94bb-02184f7bde48"))
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        return this.toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = getUser(id);

        user.setName(request.getName());

        return this.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = getUser(id);

        user.setIsActive(false);

        userRepository.save(user);
    }
}
