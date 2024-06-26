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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final Logger log = LoggerFactory.getLogger(ProductController.class);

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> add(@RequestBody ProductAddRequest request) {
    log.info("adding product {}", request);

    ProductResponse response = productService.save(request);
    log.info("product {} added", response.getId());

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    path = "/{product-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> get(@PathVariable("product-id") @ValidUUID String id,
      @RequestParam(value = "eager", required = false, defaultValue = "false") boolean eager) {
    log.info("retrieving product {}", id);

    UUID productId = UUID.fromString(id);
    ProductResponse response = productService.get(productId, eager);
    log.info("product {} retrieved", id);

    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "seller_id", required = false) UUID sellerId,
      @RequestParam(value = "min_price", required = false) Long minPrice,
      @RequestParam(value = "max_price", required = false) Long maxPrice,
      @RequestParam(value = "eager", required = false, defaultValue = "false") boolean eager,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {

    log.info("revalidate search parameters");

    if (minPrice != null && minPrice < 0) {
      log.error("min_price does not meet the requirement");
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "min_price must be greater than or equal to 0"));
    }

    if (maxPrice != null && maxPrice < 0) {
      log.error("max_price does not meet the requirement");
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "max_price must be greater than 0"));
    }

    if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
      log.error("min_price and max_price does not meet the requirement");
      return ResponseEntity.badRequest()
          .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "min_price must be less than or equal to max_price"));
    }
    log.info("search parameters are valid");

    log.info("searching products");

    ProductSearchRequest request = ProductSearchRequest.builder()
        .name(name)
        .sellerId(sellerId)
        .minPrice(BigDecimal.valueOf(minPrice))
        .maxPrice(BigDecimal.valueOf(maxPrice))
        .eager(eager)
        .page(page)
        .size(size)
        .build();

    Page<ProductResponse> result = productService.search(request);
    PagedResponse<ProductResponse> response = PagedResponse.of("products", result);

    log.info("products found: {}", result.getTotalElements());
    return ResponseEntity.ok(response);
  }

  @PutMapping(
    path = "/{product-id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> updateInfo(@PathVariable("product-id") @ValidUUID String id,
      @RequestBody ProductUpdateRequest request) {

    log.info("updating product {}", id);

    request.setId(id);
    ProductResponse response = productService.updateInfo(request);
    log.info("product {} updated", id);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(
    path = "/{product-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> deactivate(@PathVariable("product-id") @ValidUUID String id) {
    log.info("deleting product {}", id);

    UUID productId = UUID.fromString(id);
    productService.archive(productId);
    log.info("product {} deleted", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(
    path = "/{product-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> restore(@PathVariable("product-id") @ValidUUID String id) {
    log.info("restoring product {}", id);

    UUID productId = UUID.fromString(id);
    productService.restore(productId);
    log.info("product {} restored", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
