package com.hestia.api.guest.service;

import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.domain.message.entity.Message;
import com.hestia.api.domain.message.enums.MessageType;
import com.hestia.api.domain.message.repository.MessageRepository;
import com.hestia.api.domain.registry.entity.GiftAvailability;
import com.hestia.api.domain.registry.entity.Order;
import com.hestia.api.domain.registry.enums.OrderStatus;
import com.hestia.api.domain.registry.repository.GiftAvailabilityRepository;
import com.hestia.api.domain.registry.repository.GiftRepository;
import com.hestia.api.domain.registry.repository.OrderRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;
import com.hestia.api.guest.dto.CheckoutRequest;
import com.hestia.api.guest.dto.CheckoutResponse;
import com.hestia.api.infrastructure.asaas.AsaasCheckoutClient;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasCheckoutResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestCheckoutService {

    private final GiftAvailabilityRepository giftAvailabilityRepository;
    private final GiftRepository giftRepository;
    private final OrderRepository orderRepository;
    private final WeddingRepository weddingRepository;
    private final AsaasCheckoutClient asaasCheckoutClient;
    private final MessageRepository messageRepository;

    @Transactional
    public CheckoutResponse createCheckout(UUID weddingId, UUID giftId, CheckoutRequest request) {
        // 1. Validate gift exists & has stock
        GiftAvailability gift = giftAvailabilityRepository
                .findByIdAndWeddingId(giftId, weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Gift not found"));

        if (!gift.getAvailability()) throw new IllegalArgumentException("Gift is out of stock");

        // 2. Load Wedding (for FK on Order)
        Wedding wedding = weddingRepository
                .findByIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        // 3. Create Order (PENDING) — paymentId is temporary, will be updated
        Order baseOrder = Order.builder()
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .amount(gift.getPrice()) // TODO: see to pass quantity * price
                .status(OrderStatus.PENDING)
                .paymentId(UUID.randomUUID()) // placeholder, replaced after Asaas call
                .gift(giftRepository.getReferenceById(giftId))
                .wedding(wedding)
                .build();
        Order order = orderRepository.save(baseOrder);

        // 3b. Create GIFT message if provided
        if (request.getMessage() != null && !request.getMessage().isBlank()) {
            String sender = request.getSender() != null ? request.getSender() : request.getGuestName();
            Message message = Message.builder()
                    .sender(sender)
                    .message(request.getMessage())
                    .isFavorite(false)
                    .isNew(true)
                    .type(MessageType.GIFT)
                    .wedding(wedding)
                    .build();
            message = messageRepository.save(message);
            order.setMessage(message);
            orderRepository.save(order);
        }

        // 4. Call Asaas Checkout
        AsaasCheckoutRequest asaasRequest = AsaasCheckoutRequest.builder()
                .billingTypes(List.of("PIX", "CREDIT_CARD"))
                .chargeTypes(List.of("DETACHED"))
                .minutesToExpire(60)
                .externalReference(order.getId().toString())
                .callback(AsaasCheckoutRequest.Callback.builder()
                        .successUrl("https://hestia.com/payment/success") // TODO: configure per wedding
                        .cancelUrl("https://hestia.com/payment/cancel")
                        .expiredUrl("https://hestia.com/payment/expired")
                        .build())
                .items(List.of(AsaasCheckoutRequest.Item.builder()
                        .name(
                                gift.getDescription().length() > 30
                                        ? gift.getDescription().substring(0, 30)
                                        : gift.getDescription())
                        .description(gift.getDescription())
                        .quantity(1)
                        .value(gift.getPrice() / 100.0) // cents → reais
                        .build()))
                .build();

        AsaasCheckoutResponse asaasResponse = asaasCheckoutClient.createCheckout(asaasRequest);

        // 5. Save payment_id (Asaas checkout ID) on Order
        order.setPaymentId(UUID.fromString(asaasResponse.getId()));
        orderRepository.save(order);

        // 6. Return checkout URL
        return new CheckoutResponse(asaasResponse.getLink());
    }
}
