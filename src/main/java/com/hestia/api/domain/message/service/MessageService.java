package com.hestia.api.domain.message.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.message.dto.CreateMessageRequest;
import com.hestia.api.domain.message.dto.MessageResponse;
import com.hestia.api.domain.message.dto.UpdateMessageRequest;
import com.hestia.api.domain.message.entity.Message;
import com.hestia.api.domain.message.mapper.MessageMapper;
import com.hestia.api.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Transactional(readOnly = true)
    public PageResponse<MessageResponse> getMessages(Pageable pageable, Boolean isNew, Boolean isFavorite) {
        Page<MessageResponse> message;

        if (Boolean.TRUE.equals(isNew))
            message = messageRepository.findByIsNewTrueAndIsActiveTrue(pageable)
                    .map(messageMapper::toResponse);

        else if (Boolean.TRUE.equals(isFavorite))
            message = messageRepository.findByIsFavoriteTrueAndIsActiveTrue(pageable)
                    .map(messageMapper::toResponse);
        else
            message = messageRepository.findByIsActiveTrue(pageable)
                .map(messageMapper::toResponse);

        return PageMapper.toResponse(message);
    }

    private Message getMessage(UUID id) {
        return messageRepository.findByIdAndIsActiveTrue(id)
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

        return messageMapper.toResponse(messageRepository.save(message));
    }

    public MessageResponse updateMessage(UUID id, UpdateMessageRequest request) {
        Message message = getMessage(id);

        if (request.getIsFavorite() != null)
            message.setIsFavorite(request.getIsFavorite());

        return messageMapper.toResponse(messageRepository.save(message));
    }

    public MessageResponse readMessage(UUID id) {
        Message message = getMessage(id);

        message.setIsNew(false);

        return messageMapper.toResponse(messageRepository.save(message));
    }

    public void deleteMessage(UUID id) {
        Message message = getMessage(id);
        message.setIsActive(false);
        messageRepository.save(message);
    }
}
