package com.hestia.api.domain.accounts.controller;

import com.hestia.api.domain.accounts.dto.CreateUserRequest;
import com.hestia.api.domain.accounts.dto.UpdateUserRequest;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.service.UserService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@Tag(name = "Accounts")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(name = "user_id", required = false) UUID userId,
            Pageable pageable) {
        UUID resolvedId = user.resolveUserId(userId);
        if (resolvedId != null) {
            return ResponseEntity.ok(userService.getCoupleAccount(resolvedId));
        }
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping
    public ResponseEntity<?> patchAccount(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(name = "user_id", required = false) UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(user.resolveUserId(userId), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(user.resolveUserId(id), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
