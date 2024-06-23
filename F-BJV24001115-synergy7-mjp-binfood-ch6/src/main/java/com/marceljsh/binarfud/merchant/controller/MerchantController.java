package com.marceljsh.binarfud.merchant.controller;

import com.marceljsh.binarfud.common.dto.PagedResponse;
import com.marceljsh.binarfud.validation.ValidUUID;
import com.marceljsh.binarfud.merchant.dto.MerchantResponse;
import com.marceljsh.binarfud.merchant.dto.MerchantSearchRequest;
import com.marceljsh.binarfud.merchant.service.MerchantService;
import com.marceljsh.binarfud.merchant.dto.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.merchant.dto.MerchantAddRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

  private final Logger log = LoggerFactory.getLogger(MerchantController.class);

  @Autowired
  private MerchantService merchantService;

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MerchantResponse> add(@RequestBody MerchantAddRequest request) {
    log.info("Received add merchant request: {}", request);

    MerchantResponse response = merchantService.save(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MerchantResponse> get(@ValidUUID @PathVariable("id") String id) {
    log.info("Received get merchant request: {}", id);

    UUID merchantId = UUID.fromString(id);

    MerchantResponse response = merchantService.get(merchantId);

    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResponse<MerchantResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "location", required = false) String location,
      @RequestParam(value = "open", required = false) Boolean status,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    log.info("Received search merchant request: name={}, location={}, status={}, page={}, size={}",
        name, location, status, page, size);

    MerchantSearchRequest request = MerchantSearchRequest.builder()
        .name(name)
        .location(location)
        .open(status)
        .page(page)
        .size(size)
        .build();

    Page<MerchantResponse> result = merchantService.search(request);
    PagedResponse<MerchantResponse> body = PagedResponse.from(result);

    return ResponseEntity.ok(body);
  }

  @PutMapping(
    value = "/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MerchantResponse> updateInfo(@ValidUUID @PathVariable("id") String id,
      @RequestBody MerchantUpdateInfoRequest request) {
    log.info("Received update merchant info: {}", id);

    UUID merchantId = UUID.fromString(id);
    request.setId(merchantId);

    MerchantResponse response = merchantService.updateInfo(request);

    return ResponseEntity.ok(response);
  }

  @PatchMapping(
    value = "/{id}/open",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> open(@ValidUUID @PathVariable("id") String id) {
    log.info("Received open merchant request: {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.updateStatus(merchantId, true);

    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}/close")
  public ResponseEntity<Void> close(@ValidUUID @PathVariable("id") String id) {
    log.info("Received close merchant request: {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.updateStatus(merchantId, false);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deactivate(@ValidUUID @PathVariable("id") String id) {
    log.info("Received delete merchant request: {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.deactivate(merchantId);

    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{id}/restore")
  public ResponseEntity<Void> restore(@ValidUUID @PathVariable("id") String id) {
    log.info("Received restore merchant request: {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.restore(merchantId);

    return ResponseEntity.ok().build();
  }

}
