package com.hestia.api.infrastructure.asaas.service;

import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.repository.WalletRepository;
import com.hestia.api.domain.registry.entity.Order;
import com.hestia.api.domain.registry.enums.OrderStatus;
import com.hestia.api.domain.registry.repository.OrderRepository;
import com.hestia.api.infrastructure.asaas.dto.AsaasWebhookPayload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AsaasWebhookService {

    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final String configuredWebhookToken;

    public AsaasWebhookService(
            OrderRepository orderRepository,
            WalletRepository walletRepository,
            @Value("${asaas.webhook-token}") String configuredWebhookToken) {
        this.orderRepository = orderRepository;
        this.walletRepository = walletRepository;
        this.configuredWebhookToken = configuredWebhookToken;
    }

    @Transactional
    public void handleWebhook(String webhookToken, AsaasWebhookPayload payload) {
        // 1. Validate webhook token against configured global token
        if (!webhookToken.equals(configuredWebhookToken)) {
            throw new ResourceNotFoundException("Invalid webhook token");
        }

        // 2. Extract checkout ID
        String checkoutId = payload.getCheckout().getId();
        UUID paymentId = UUID.fromString(checkoutId);

        // 3. Find Order by payment_id
        Order order = orderRepository
                .findByPaymentIdAndIsActiveTrue(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for payment: " + checkoutId));

        // 4. Idempotency: skip if already processed
        if (order.getStatus() != OrderStatus.PENDING) {
            return;
        }

        // 5. Handle event
        switch (payload.getEvent()) {
            case "CHECKOUT_PAID" -> {
                order.setStatus(OrderStatus.PAID);
                // Credit wallet balance with gross order amount
                Optional<Wallet> wallet =
                        walletRepository.findFirstByWeddingIdAndIsActiveTrue(order.getWedding().getId());
                wallet.ifPresent(w -> {
                    w.setBalance(w.getBalance() + order.getAmount());
                    walletRepository.save(w);
                });
            }
            case "CHECKOUT_EXPIRED" -> order.setStatus(OrderStatus.EXPIRED);
            case "CHECKOUT_CANCELED" -> order.setStatus(OrderStatus.FAILED);
            default -> {
                return;
            } // ignore unknown events
        }

        orderRepository.save(order);
    }
}
