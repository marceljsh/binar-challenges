package com.marceljsh.binarfud.product.service;

import com.marceljsh.binarfud.product.dto.ProductUpdateInfoRequest;
import com.marceljsh.binarfud.product.dto.ProductAddRequest;
import com.marceljsh.binarfud.product.dto.ProductResponse;
import com.marceljsh.binarfud.product.dto.ProductSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {

  ProductResponse save(ProductAddRequest request);

  void archive(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  ProductResponse get(UUID id);

  Page<ProductResponse> search(ProductSearchRequest request);

  ProductResponse updateInfo(ProductUpdateInfoRequest request);

}
