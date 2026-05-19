package com.hestia.api.common.mapper;

import com.hestia.api.common.dto.PageResponse;

import org.springframework.data.domain.Page;

public class PageMapper {

    public static <T> PageResponse<T> toResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .data(page.getContent())
                .meta(PageResponse.Meta.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .hasNext(page.hasNext())
                        .hasPrevious(page.hasPrevious())
                        .build())
                .build();
    }
}
