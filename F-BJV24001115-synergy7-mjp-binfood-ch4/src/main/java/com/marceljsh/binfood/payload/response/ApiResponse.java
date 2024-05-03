package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

  private T data;

  private String error;

  private PagingResponse paging;
}
