package com.hestia.api.domain.accounts.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.accounts.dto.UserResponse;
import com.hestia.api.domain.accounts.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
