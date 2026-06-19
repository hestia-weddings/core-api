package com.hestia.api.guest.controller;

import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.enums.MessageType;
import com.hestia.api.domain.message.service.MessageService;
import com.hestia.api.domain.wedding.service.WeddingService;
import com.hestia.api.guest.dto.GiftMessageResponse;
import com.hestia.api.guest.dto.InviteMessageResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/w/{slug}/message")
@Tag(name = "Messages")
@RequiredArgsConstructor
public class GuestMessageController {

    private final MessageService messageService;
    private final WeddingService weddingService;

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(
            @RequestAttribute UUID weddingId, @Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(weddingId, request, MessageType.GENERAL));
    }

    @GetMapping("/invite")
    public ResponseEntity<InviteMessageResponse> getInviteMessage(@RequestAttribute UUID weddingId) {
        return ResponseEntity.ok(weddingService.getInviteMessage(weddingId));
    }

    @GetMapping("/gift")
    public ResponseEntity<GiftMessageResponse> getGiftMessage(@RequestAttribute UUID weddingId) {
        return ResponseEntity.ok(weddingService.getGiftMessage(weddingId));
    }
}
