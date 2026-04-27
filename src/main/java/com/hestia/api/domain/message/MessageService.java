package com.hestia.api.domain.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message createMessage(String sender, String content) {
        Message message = Message.builder()
                .sender(sender)
                .message(content)
                .isFavorite(false)
                .isNew(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessages(Boolean isNew, Boolean isFavorite) {
        if (Boolean.TRUE.equals(isNew))
            return messageRepository.findByIsNewTrue();
        
        if (Boolean.TRUE.equals(isFavorite))
            return messageRepository.findByIsFavoriteTrue();

        return messageRepository.findByIsActiveTrue();
    }

    public Message getMessageById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    public Message updateMessage(UUID id, Boolean isFavorite, Boolean isNew) {
        Message message = getMessageById(id);

        message.setIsFavorite(isFavorite);
        message.setIsNew(isNew);
        message.setUpdatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public void deleteMessage(UUID id) {
        Message message = getMessageById(id);

        message.setIsActive(false);
        message.setUpdatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }
}
