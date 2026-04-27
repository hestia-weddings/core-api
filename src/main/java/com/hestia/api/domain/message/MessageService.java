package com.hestia.api.domain.message;

import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(message.getSender())
                .message(message.getMessage())
                .isFavorite(message.getIsFavorite())
                .isNew(message.getIsNew())
                .createdAt(message.getCreatedAt())
                .build();
    }

    public List<MessageResponse> getMessages(Boolean isNew, Boolean isFavorite) {
        List<Message> messages;

        if (Boolean.TRUE.equals(isNew))
            messages = messageRepository.findByIsNewTrue();
        else if (Boolean.TRUE.equals(isFavorite))
            messages = messageRepository.findByIsFavoriteTrue();
        else
            messages = messageRepository.findByIsActiveTrue();

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    public Message getMessageById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    public MessageResponse createMessage(CreateMessageRequest request) {
        Message message = Message.builder()
                .sender(request.getSender())
                .message(request.getMessage())
                .isFavorite(false)
                .isNew(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .weddingId(UUID.fromString("7987490b-ed02-4e3f-87df-4e063eeed604"))
                .build();

        return this.toResponse(messageRepository.save(message));
    }

    public MessageResponse updateMessage(UUID id, UpdateMessageRequest request) {
        Message message = getMessageById(id);

        if (request.getIsFavorite() != null)
            message.setIsFavorite(request.getIsFavorite());
        if (request.getIsNew() != null)
            message.setIsNew(request.getIsNew());

        message.setUpdatedAt(LocalDateTime.now());

        return this.toResponse(messageRepository.save(message));
    }

    public void deleteMessage(UUID id) {
        Message message = getMessageById(id);

        message.setIsActive(false);
        message.setUpdatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }
}
