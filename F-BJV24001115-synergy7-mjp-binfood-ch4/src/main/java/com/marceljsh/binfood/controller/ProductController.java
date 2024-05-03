package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.ProductAddRequest;
import com.marceljsh.binfood.payload.request.ProductSearchRequest;
import com.marceljsh.binfood.payload.request.ProductUpdateRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.PagingResponse;
import com.marceljsh.binfood.payload.response.ProductResponse;
import com.marceljsh.binfood.service.spec.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<ProductResponse> add(User user, @RequestBody ProductAddRequest request) {
    ProductResponse response = productService.add(request);
    return ApiResponse.<ProductResponse>builder().data(response).build();
  }

  @GetMapping(path = "/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductResponse get(User user, @PathVariable("product-id") String id) {
    return productService.findById(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<List<ProductResponse>> search(User user, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "min-price", defaultValue = "0") Long minPrice, @RequestParam(value = "max-price", defaultValue = "-1") Long maxPrice, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {

    if (maxPrice == -1) {
      maxPrice = Long.MAX_VALUE;
    }

    ProductSearchRequest request = ProductSearchRequest.builder()
        .name(name)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .page(page)
        .size(size)
        .build();

    Page<ProductResponse> responses = productService.search(request);

    return ApiResponse.<List<ProductResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
          .currentPage(responses.getNumber())
          .totalPages(responses.getTotalPages())
          .size(responses.getSize())
          .build())
        .build();
  }

  @PutMapping(path = "/{product-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<ProductResponse> update(User user, @PathVariable("product-id") String id, @RequestBody ProductUpdateRequest request) {
    request.setId(id);
    ProductResponse response = productService.update(request);
    return ApiResponse.<ProductResponse>builder().data(response).build();
  }

  @DeleteMapping(path = "/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> delete(User user, @PathVariable("product-id") String id) {
    productService.remove(id);
    return ApiResponse.<String>builder().data("OK").build();
  }
}
