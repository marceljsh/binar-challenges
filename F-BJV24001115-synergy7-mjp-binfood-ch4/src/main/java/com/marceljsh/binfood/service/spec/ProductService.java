package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.payload.request.ProductAddRequest;
import com.marceljsh.binfood.payload.request.ProductSearchRequest;
import com.marceljsh.binfood.payload.request.ProductUpdateRequest;
import com.marceljsh.binfood.payload.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

  ProductResponse add(ProductAddRequest request);
  void remove(String idString);
  ProductResponse findById(String idString);
  Page<ProductResponse> search(ProductSearchRequest request);
  ProductResponse update(ProductUpdateRequest request);
}
