package com.hestia.api.domain.message;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/message")
@Tag(name = "Messages", description = "CRUD operations for messages")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public PageResponse<MessageResponse> getMessage(
            Pageable pageable,
            @RequestParam(required = false, name = "is_new") Boolean isNew,
            @RequestParam(required = false, name = "is_favorite") Boolean isFavorite
    ) {
        return messageService.getMessages(pageable, isNew, isFavorite);
    }

    @PostMapping
    public MessageResponse createMessage(@RequestBody CreateMessageRequest request) {
        return messageService.createMessage(request);
    }

    @PatchMapping("/{id}")
    public MessageResponse updateMessage(
            @PathVariable UUID id,
            @RequestBody UpdateMessageRequest request
        ) {
        return messageService.updateMessage(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
    }
}
