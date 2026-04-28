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
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
    private boolean last;
}
