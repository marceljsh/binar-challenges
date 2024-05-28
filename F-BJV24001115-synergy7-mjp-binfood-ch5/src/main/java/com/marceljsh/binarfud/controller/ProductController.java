package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.ProductAddRequest;
import com.marceljsh.binarfud.payload.request.ProductSearchRequest;
import com.marceljsh.binarfud.payload.request.ProductUpdateRequest;
import com.marceljsh.binarfud.payload.response.ErrorResponse;
import com.marceljsh.binarfud.payload.response.PagedResponse;
import com.marceljsh.binarfud.payload.response.ProductResponse;
import com.marceljsh.binarfud.service.spec.ProductService;
import com.marceljsh.binarfud.util.Constants;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

  private static final Logger log = LoggerFactory.getLogger(ProductController.class);

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> add(@Valid @RequestBody ProductAddRequest request) {
    log.info("adding product {}", request);

    ProductResponse response = productService.save(request);
    log.info("product {} added", response.getId());

    return ResponseEntity.ok(response);
  }

  @GetMapping(path = "/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> get(@PathVariable("product-id") @Valid @ValidUUID String id,
      @RequestParam(value = "eager", required = false, defaultValue = "false") boolean eager) {
    log.info("retrieving product {}", id);

    UUID productId = UUID.fromString(id);
    ProductResponse response = productService.findById(productId, eager);
    log.info("product {} retrieved", id);

    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "seller_id") String sellerId, @RequestParam(value = "min_price") Long minPrice,
      @RequestParam(value = "max_price") Long maxPrice,
      @RequestParam(value = "eager", required = false, defaultValue = "false") boolean eager,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {
    if (minPrice != null && minPrice < 0) {
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "min_price must be greater than or equal to 0"));
    }

    if (maxPrice != null && maxPrice < 0) {
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "max_price must be greater than 0"));
    }

    if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "min_price must be less than or equal to max_price"));
    }

    log.info("searching products");

    ProductSearchRequest request = ProductSearchRequest.builder()
        .name(name)
        .sellerId(sellerId)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .eager(eager)
        .page(page)
        .size(size)
        .build();

    Page<ProductResponse> result = productService.get(request);
    PagedResponse<ProductResponse> response = PagedResponse.of("products", result);

    return ResponseEntity.ok(response);
  }

  @PutMapping(path = "/{product-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> update(@PathVariable("product-id") @Valid @ValidUUID String id,
      @Valid @RequestBody ProductUpdateRequest request) {
    log.info("updating product {}", id);

    request.setId(id);
    ProductResponse response = productService.updateInfo(request);
    log.info("product {} updated", id);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(path = "/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> delete(@PathVariable("product-id") @Valid @ValidUUID String id) {
    log.info("deleting product {}", id);

    UUID productId = UUID.fromString(id);
    productService.softDelete(productId);
    log.info("product {} deleted", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
