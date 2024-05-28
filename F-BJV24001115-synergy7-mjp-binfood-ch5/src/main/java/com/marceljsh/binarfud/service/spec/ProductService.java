package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.ProductAddRequest;
import com.marceljsh.binarfud.payload.request.ProductSearchRequest;
import com.marceljsh.binarfud.payload.request.ProductUpdateRequest;
import com.marceljsh.binarfud.payload.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {

  ProductResponse save(ProductAddRequest request);

  void softDelete(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  ProductResponse findById(UUID id, boolean eager);

  Page<ProductResponse> get(ProductSearchRequest request);

  ProductResponse updateInfo(ProductUpdateRequest request);
}
