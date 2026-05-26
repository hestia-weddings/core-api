package com.hestia.api.domain.payment.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.payment.dto.CreatePaymentConfigRequest;
import com.hestia.api.domain.payment.dto.PaymentConfigResponse;
import com.hestia.api.domain.payment.dto.UpdatePaymentConfigRequest;
import com.hestia.api.domain.payment.entity.PaymentConfig;
import com.hestia.api.domain.payment.mapper.PaymentConfigMapper;
import com.hestia.api.domain.payment.repository.PaymentConfigRepository;
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
public class PaymentConfigService {

    private final PaymentConfigRepository paymentConfigRepository;
    private final PaymentConfigMapper paymentConfigMapper;
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<PaymentConfigResponse> getPaymentConfigs(
            @Nullable UUID weddingId, Pageable pageable) {
        Page<PaymentConfig> page;

        if (weddingId == null) page = paymentConfigRepository.findAll(pageable);
        else page = paymentConfigRepository.findByWeddingIdAndIsActiveTrue(weddingId, pageable);

        return PageMapper.toResponse(page.map(paymentConfigMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PaymentConfigResponse getPaymentConfigById(@Nullable UUID weddingId, UUID id) {
        return paymentConfigMapper.toResponse(getPaymentConfig(weddingId, id));
    }

    private PaymentConfig getPaymentConfig(@Nullable UUID weddingId, UUID id) {
        if (weddingId == null)
            return paymentConfigRepository
                    .findByIdAndIsActiveTrue(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment Config not found"));
        return paymentConfigRepository
                .findByIdAndWeddingIdAndIsActiveTrue(id, weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Config not found"));
    }

    public PaymentConfigResponse createPaymentConfig(UUID weddingId, CreatePaymentConfigRequest request) {
        Wedding wedding = weddingRepository
                .findByIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        PaymentConfig paymentConfig = PaymentConfig.builder()
                .apiKey(request.getApiKey())
                .environment(request.getEnvironment())
                .webhookToken(request.getWebhookToken())
                .wedding(wedding)
                .build();

        return paymentConfigMapper.toResponse(paymentConfigRepository.save(paymentConfig));
    }

    public PaymentConfigResponse updatePaymentConfig(
            @Nullable UUID weddingId, UUID id, UpdatePaymentConfigRequest request) {
        PaymentConfig paymentConfig = getPaymentConfig(weddingId, id);

        if (request.getApiKey() != null) paymentConfig.setApiKey(request.getApiKey());
        if (request.getEnvironment() != null) paymentConfig.setEnvironment(request.getEnvironment());
        if (request.getWebhookToken() != null) paymentConfig.setWebhookToken(request.getWebhookToken());

        return paymentConfigMapper.toResponse(paymentConfigRepository.save(paymentConfig));
    }
}
