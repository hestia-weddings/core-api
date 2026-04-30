package com.hestia.api.domain.message;

import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                .build();
    }

    public Page<MessageResponse> getMessages(Pageable pageable, Boolean isNew, Boolean isFavorite) {
        if (Boolean.TRUE.equals(isNew))
            return messageRepository.findByIsNewTrueAndIsActiveTrue(pageable)
                    .map(this::toResponse);

        if (Boolean.TRUE.equals(isFavorite))
            return messageRepository.findByIsFavoriteTrueAndIsActiveTrue(pageable)
                    .map(this::toResponse);

        return messageRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);
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

        return this.toResponse(messageRepository.save(message));
    }

    public void deleteMessage(UUID id) {
        Message message = getMessageById(id);

        message.setIsActive(false);

        messageRepository.save(message);
    }
}
