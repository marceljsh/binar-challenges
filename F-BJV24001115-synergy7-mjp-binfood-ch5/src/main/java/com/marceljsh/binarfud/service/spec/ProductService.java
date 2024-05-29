package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.ProductAddRequest;
import com.marceljsh.binarfud.payload.request.ProductSearchRequest;
import com.marceljsh.binarfud.payload.request.ProductUpdateRequest;
import com.marceljsh.binarfud.payload.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {

  ProductResponse save(ProductAddRequest request);

  void archive(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  ProductResponse get(UUID id, boolean eager);

  Page<ProductResponse> search(ProductSearchRequest request);

  ProductResponse updateInfo(ProductUpdateRequest request);
}
