package com.marceljsh.binarfud.common.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PagedResponse<T> {

  private List<T> records;

  private int currentPage;

  private int totalPages;

  private long size;

  public static <T> PagedResponse<T> from(Page<T> page) {
    return PagedResponse.<T>builder()
        .records(page.getContent())
        .currentPage(page.getNumber() + 1)
        .totalPages(page.getTotalPages())
        .size(page.getSize())
        .build();
  }

}
