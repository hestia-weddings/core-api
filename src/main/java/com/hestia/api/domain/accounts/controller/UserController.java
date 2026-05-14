package com.hestia.api.domain.accounts.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.accounts.dto.CreateUserRequest;
import com.hestia.api.domain.accounts.dto.UpdateUserRequest;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.service.UserService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@Tag(name = "Accounts", description = "CRUD operations for accounts management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers (
            @AuthenticationPrincipal AuthenticatedUser user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsers(user.getWeddingId(), pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponse> postUser(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(user.getWeddingId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(user.getWeddingId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        userService.deleteUser(user.getWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
