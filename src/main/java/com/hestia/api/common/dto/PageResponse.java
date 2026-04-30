package com.hestia.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> data;
    private Meta meta;

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Meta {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }

}
