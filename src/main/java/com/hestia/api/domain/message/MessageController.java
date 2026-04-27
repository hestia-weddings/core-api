package com.hestia.api.domain.message;

import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<MessageResponse> getMessage(
            @RequestParam(required = false, name = "is_new") Boolean isNew,
            @RequestParam(required = false, name = "is_favorite") Boolean isFavorite
    ) {
        return messageService.getMessages(isNew, isFavorite);
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
