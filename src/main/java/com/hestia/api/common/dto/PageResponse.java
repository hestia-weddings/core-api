package com.hestia.api.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> data,
        Meta meta
) {
    @Builder
    public record Meta(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext,
            boolean hasPrevious
    ) {}
}
