package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingResponse {

  private Integer currentPage;

  private Integer totalPages;

  private Integer size;
}
