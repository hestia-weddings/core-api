package com.hestia.api.common.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record PageResponse<T>(List<T> data, Meta meta) {
    @Builder
    public record Meta(int page, int size, long totalElements, int totalPages, boolean hasNext, boolean hasPrevious) {}
}
