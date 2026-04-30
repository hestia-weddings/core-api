package com.hestia.api.domain.message.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import com.hestia.api.domain.message.entity.Message;
import com.hestia.api.domain.message.repository.MessageRepository;
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

    public PageResponse<MessageResponse> getMessages(Pageable pageable, Boolean isNew, Boolean isFavorite) {
        Page<MessageResponse> message;

        if (Boolean.TRUE.equals(isNew))
            message = messageRepository.findByIsNewTrueAndIsActiveTrue(pageable)
                    .map(this::toResponse);

        else if (Boolean.TRUE.equals(isFavorite))
            message = messageRepository.findByIsFavoriteTrueAndIsActiveTrue(pageable)
                    .map(this::toResponse);
        else
            message = messageRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);

        return PageMapper.toResponse(message);
    }

    private Message getMessage(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
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
        Message message = getMessage(id);

        if (request.getIsFavorite() != null)
            message.setIsFavorite(request.getIsFavorite());
        if (request.getIsNew() != null)
            message.setIsNew(request.getIsNew());

        return this.toResponse(messageRepository.save(message));
    }

    public void deleteMessage(UUID id) {
        Message message = getMessage(id);

        message.setIsActive(false);

        messageRepository.save(message);
    }
}
