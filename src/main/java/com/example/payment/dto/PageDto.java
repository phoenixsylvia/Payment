package com.example.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@Builder
public class PageDto {

    private List<?> data;

    private boolean hasMore;

    private int currentPage;

    private int totalPages;

    private long totalItems;

    public static PageDto build(Page<?> page, List<?> data) {
        return PageDto.builder()
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalItems(page.getTotalElements())
                .hasMore(page.hasNext())
                .data(data)
                .build();

    }
}
