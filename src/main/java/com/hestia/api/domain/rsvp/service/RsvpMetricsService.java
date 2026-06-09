package com.hestia.api.domain.rsvp.service;

import com.hestia.api.domain.rsvp.dto.RsvpMetricsResponse;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.rsvp.repository.GuestRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RsvpMetricsService {

    private static final Logger log = LoggerFactory.getLogger(RsvpMetricsService.class);
    private final GuestRepository guestRepository;

    public RsvpMetricsResponse generateRsvpMetrics(UUID weddingId) {
        List<Guest> guests = guestRepository.findByIsActiveTrue();

        log.info("Guests found for wedding {}: {}", weddingId, guests.size());
        guests.forEach(
                g -> log.info("  Guest: {} | status={} | ageGroup={}", g.getName(), g.getStatus(), g.getAgeGroup()));

        return RsvpMetricsResponse.builder()
                .total(guests.size())
                .status(RsvpMetricsResponse.StatusMetrics.builder()
                        .pending((int) guests.stream()
                                .filter(g -> g.getStatus() == GuestStatus.PENDING)
                                .count())
                        .confirmed((int) guests.stream()
                                .filter(g -> g.getStatus() == GuestStatus.CONFIRMED)
                                .count())
                        .declined((int) guests.stream()
                                .filter(g -> g.getStatus() == GuestStatus.DECLINED)
                                .count())
                        .build())
                .ageGroup(RsvpMetricsResponse.AgeGroupMetrics.builder()
                        .baby((int) guests.stream()
                                .filter(g -> g.getAgeGroup() == GuestAge.BABY)
                                .count())
                        .child((int) guests.stream()
                                .filter(g -> g.getAgeGroup() == GuestAge.CHILD)
                                .count())
                        .adult((int) guests.stream()
                                .filter(g -> g.getAgeGroup() == GuestAge.ADULT)
                                .count())
                        .build())
                .build();
    }
}
