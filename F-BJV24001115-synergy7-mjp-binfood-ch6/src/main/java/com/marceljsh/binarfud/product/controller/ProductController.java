package com.marceljsh.binarfud.product.controller;

import com.marceljsh.binarfud.common.dto.PagedResponse;
import com.marceljsh.binarfud.validation.ValidUUID;
import com.marceljsh.binarfud.product.dto.ProductResponse;
import com.marceljsh.binarfud.product.dto.ProductSearchRequest;
import com.marceljsh.binarfud.product.service.ProductService;
import com.marceljsh.binarfud.product.dto.ProductUpdateInfoRequest;
import com.marceljsh.binarfud.product.dto.ProductAddRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final Logger log = LoggerFactory.getLogger(ProductController.class);

  private final ProductService productService;

  @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_GOD')")
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> add(@RequestBody ProductAddRequest request) {
    log.info("Received add product request: {} by {}", request.getName(), request.getSellerId());

    ProductResponse body = productService.save(request);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> get(@ValidUUID @PathVariable("id") String id) {
    log.info("Received get product request: {}", id);

    UUID productId = UUID.fromString(id);

    ProductResponse body = productService.get(productId);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResponse<ProductResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "min-price", defaultValue = "0") BigDecimal minPrice,
      @RequestParam(value = "max-price", defaultValue = "0") BigDecimal maxPrice,
      @RequestParam(value = "seller", required = false) String seller,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    log.info("Received search product request: name={}, minPrice={}, maxPrice={}, seller={}, page={}, size={}",
        name, minPrice, maxPrice, seller, page, size);

    if (maxPrice.compareTo(BigDecimal.ZERO) == 0) {
      maxPrice = new BigDecimal(Long.MAX_VALUE);
    }

    ProductSearchRequest request = ProductSearchRequest.builder()
        .name(name)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .seller(seller)
        .page(page - 1)
        .size(size)
        .build();

    Page<ProductResponse> result = productService.search(request);
    PagedResponse<ProductResponse> body = PagedResponse.from(result);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_GOD')")
  @PutMapping(
    value = "/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ProductResponse> updateInfo(@ValidUUID @PathVariable("id") String id,
      @RequestBody ProductUpdateInfoRequest request) {
    log.info("Received update product info request: {}", id);

    UUID productId = UUID.fromString(id);
    request.setId(productId);

    ProductResponse body = productService.updateInfo(request);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_GOD')")
  @DeleteMapping(value = "/{id}/archive")
  public ResponseEntity<Void> archive(@ValidUUID @PathVariable("id") String id) {
    log.info("Received archive product request: {}", id);

    UUID productId = UUID.fromString(id);

    productService.archive(productId);

    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('ROLE_MERCHANT', 'ROLE_GOD')")
  @PatchMapping(value = "/{id}/restore")
  public ResponseEntity<Void> restore(@ValidUUID @PathVariable("id") String id) {
    log.info("Received restore product request: {}", id);

    UUID productId = UUID.fromString(id);

    productService.restore(productId);

    return ResponseEntity.ok().build();
  }
}
