package com.hestia.api.domain.accounts.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.accounts.dto.CreateUserRequest;
import com.hestia.api.domain.accounts.dto.UpdateUserRequest;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "CRUD operations for users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers (Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
