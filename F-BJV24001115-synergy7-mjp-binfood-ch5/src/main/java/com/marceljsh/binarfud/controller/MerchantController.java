package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantSearchRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import com.marceljsh.binarfud.payload.response.PagedResponse;
import com.marceljsh.binarfud.service.spec.MerchantService;
import com.marceljsh.binarfud.util.Constants;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import jakarta.validation.Valid;
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

import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/merchants")
public class MerchantController {

  private final MerchantService merchantService;
  private static final Logger log = LoggerFactory.getLogger(MerchantController.class);

  @Autowired
  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MerchantResponse> add(@Valid @RequestBody MerchantAddRequest request) {
    log.info("adding merchant {}", request);

    MerchantResponse response = merchantService.save(request);
    log.info("merchant {} added", response.getId());

    return ResponseEntity.ok(response);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MerchantResponse> get(@PathVariable("id") @Valid @ValidUUID String id) {
    log.info("retrieving merchant {}", id);

    UUID merchantId = UUID.fromString(id);
    MerchantResponse response = merchantService.findById(merchantId);
    log.info("merchant {} retrieved", merchantId);

    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResponse<MerchantResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "location", required = false) String location,
      @RequestParam(value = "open", required = false) Boolean status,
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
    log.info("searching merchants with [name: {} | location: {} | page: {} | size: {}]", name, location, page, size);

    MerchantSearchRequest request = MerchantSearchRequest.builder()
        .name(name)
        .location(location)
        .open(status)
        .page(page - 1)
        .size(size)
        .build();

    Page<MerchantResponse> result = merchantService.get(request);
    PagedResponse<MerchantResponse> response = PagedResponse.of("merchants", result);

    return ResponseEntity.ok(response);
  }

  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MerchantResponse> updateInfo(@Valid @ValidUUID @PathVariable("id") String id,
      @Valid @RequestBody MerchantUpdateInfoRequest request) {
    log.info("updating merchant {}", request);

    request.setId(id);
    MerchantResponse response = merchantService.updateInfo(request);
    log.info("merchant {} info updated", id);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> delete(@PathVariable("id") @Valid @ValidUUID String id) {
    log.info("deleting merchant {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.softDelete(merchantId);
    log.info("merchant {} deleted", merchantId);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(path = "/{id}")
  public ResponseEntity<Map<String, String>> restore(@PathVariable("id") @Valid @ValidUUID String id) {
    log.info("restoring merchant {}", id);

    UUID merchantId = UUID.fromString(id);
    merchantService.restore(merchantId);
    log.info("merchant {} restored", merchantId);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(path = "/{id}/open")
  public ResponseEntity<Map<String, String>> openMerchant(@PathVariable("id") @Valid @ValidUUID String id) {
    log.info("opening merchant {}", id);

    merchantService.updateStatus(UUID.fromString(id), true);
    log.info("merchant {} status set to open", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(path = "/{id}/close")
  public ResponseEntity<Map<String, String>> closeMerchant(@PathVariable("id") @Valid @ValidUUID String id) {
    log.info("closing merchant {}", id);

    merchantService.updateStatus(UUID.fromString(id), false);
    log.info("merchant {} status set to closed", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
