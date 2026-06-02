package com.hestia.api.domain.registry.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.registry.dto.OrderResponse;
import com.hestia.api.domain.registry.service.OrderService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@Tag(name = "Registry")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(user.resolveWeddingId(wedding), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(user.resolveWeddingId(), id));
    }
}
