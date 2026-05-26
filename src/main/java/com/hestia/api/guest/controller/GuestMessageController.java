package com.hestia.api.guest.controller;

import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.service.MessageService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/w/{slug}/message")
@Tag(name = "Guest", description = "Public guest-facing endpoints")
@RequiredArgsConstructor
public class GuestMessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(
            @RequestAttribute UUID weddingId, @Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(weddingId, request));
    }
}
