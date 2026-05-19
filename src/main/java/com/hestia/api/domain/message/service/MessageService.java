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
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;
import jakarta.annotation.Nullable;
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
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<MessageResponse> getMessages(
            @Nullable UUID weddingId,
            Boolean isNew,
            Boolean isFavorite,
            Pageable pageable
    ) {
        Page<Message> page;

        if (weddingId == null)
            page = messageRepository.findAll(pageable);
        else {
            if (Boolean.TRUE.equals(isNew))
                page = messageRepository.findByWeddingIdAndIsNewTrueAndIsActiveTrue(weddingId, pageable);
            else if (Boolean.TRUE.equals(isFavorite))
                page = messageRepository.findByWeddingIdAndIsFavoriteTrueAndIsActiveTrue(weddingId, pageable);
            else
                page = messageRepository.findByWeddingIdAndIsActiveTrue(weddingId, pageable);
        }

        return PageMapper.toResponse(page.map(messageMapper::toResponse));
    }

    private Message getMessage(UUID weddingId, UUID id) {
        return messageRepository.findByIdAndWeddingIdAndIsActiveTrue(id, weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
    }

    public MessageResponse createMessage(UUID weddingId, CreateMessageRequest request) {
        Wedding wedding = weddingRepository.findByIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        Message message = Message.builder()
                .sender(request.getSender())
                .message(request.getMessage())
                .isFavorite(false)
                .isNew(true)
                .wedding(wedding)
                .build();

        return messageMapper.toResponse(messageRepository.save(message));
    }

    public MessageResponse updateMessage(UUID weddingId, UUID id, UpdateMessageRequest request) {
        Message message = getMessage(weddingId, id);

        if (request.getIsFavorite() != null)
            message.setIsFavorite(request.getIsFavorite());

        return messageMapper.toResponse(messageRepository.save(message));
    }

    public MessageResponse readMessage(UUID weddingId, UUID id) {
        Message message = getMessage(weddingId, id);
        message.setIsNew(false);
        return messageMapper.toResponse(messageRepository.save(message));
    }

    public void deleteMessage(UUID weddingId, UUID id) {
        Message message = getMessage(weddingId, id);
        message.setIsActive(false);
        messageRepository.save(message);
    }
}
