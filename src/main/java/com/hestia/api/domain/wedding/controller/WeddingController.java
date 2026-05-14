package com.hestia.api.domain.wedding.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.wedding.dto.CreateWeddingRequest;
import com.hestia.api.domain.wedding.dto.UpdateWeddingRequest;
import com.hestia.api.domain.wedding.dto.WeddingResponse;
import com.hestia.api.domain.wedding.service.WeddingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wedding")
@Tag(name = "Weddings", description = "CRUD operations for weddings management")
@RequiredArgsConstructor
public class WeddingController {

    private final WeddingService weddingService;

    @GetMapping
    public ResponseEntity<PageResponse<WeddingResponse>> getWeddings (Pageable pageable) {
        return ResponseEntity.ok(weddingService.getWeddings(pageable));
    }

    @PostMapping
    public ResponseEntity<WeddingResponse> postWedding(@Valid @RequestBody CreateWeddingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weddingService.createWedding(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingResponse> getWeddingById(@PathVariable UUID id) {
        return ResponseEntity.ok(weddingService.getWeddingById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WeddingResponse> patchUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWeddingRequest request
    ) {
        return ResponseEntity.ok(weddingService.updateWedding(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        weddingService.deleteWedding(id);
        return ResponseEntity.noContent().build();
    }
}
