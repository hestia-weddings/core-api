package com.hestia.api.domain.rsvp.dto;

import lombok.Builder;

@Builder
public record RsvpMetricsResponse(Integer total, StatusMetrics status, AgeGroupMetrics ageGroup) {

    @Builder
    public record StatusMetrics(Integer pending, Integer confirmed, Integer declined) {}

    @Builder
    public record AgeGroupMetrics(Integer baby, Integer child, Integer adult) {}
}
