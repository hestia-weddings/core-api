package com.hestia.api.domain.message.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.message.service.MessageService;
import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/message")
@Tag(name = "Messages", description = "CRUD operations for messages management")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<PageResponse<MessageResponse>> getMessage(
            Pageable pageable,
            @RequestParam(required = false, name = "is_new") Boolean isNew,
            @RequestParam(required = false, name = "is_favorite") Boolean isFavorite
    ) {
        return ResponseEntity.ok(messageService.getMessages(pageable, isNew, isFavorite));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(
            @PathVariable UUID id,
            @RequestBody UpdateMessageRequest request
        ) {
        return ResponseEntity.ok(messageService.updateMessage(id, request));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageResponse> readMessage(
            @PathVariable UUID id
        ) {
        return ResponseEntity.ok(messageService.readMessage(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
