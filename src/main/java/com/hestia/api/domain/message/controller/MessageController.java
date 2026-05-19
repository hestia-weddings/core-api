package com.hestia.api.domain.message.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.message.service.MessageService;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/message")
@Tag(name = "Messages", description = "CRUD operations for messages management")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<PageResponse<MessageResponse>> getMessage(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false, name = "is_new") Boolean isNew,
            @RequestParam(required = false, name = "is_favorite") Boolean isFavorite,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable
    ) {
        return ResponseEntity.ok(messageService.getMessages(user.resolveWeddingId(wedding), isNew, isFavorite, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(messageService.getMessageById(user.resolveWeddingId(), id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody UpdateMessageRequest request,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(messageService.updateMessage(user.resolveWeddingId(), id, request));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageResponse> readMessage(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(messageService.readMessage(user.resolveWeddingId(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id
    ) {
        messageService.deleteMessage(user.resolveWeddingId(), id);
        return ResponseEntity.noContent().build();
    }
}
