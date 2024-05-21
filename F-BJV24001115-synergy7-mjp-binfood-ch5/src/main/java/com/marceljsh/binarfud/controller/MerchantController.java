package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import com.marceljsh.binarfud.service.spec.MerchantService;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("/merchants")
public class MerchantController {

  private final MerchantService merchantService;

  @Autowired
  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MerchantResponse> add(@Valid @RequestBody MerchantAddRequest request) {
    MerchantResponse response = merchantService.save(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping(path = "/{merchant-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MerchantResponse> get(@PathVariable("merchant-id") @Valid @ValidUUID String id) {
    UUID merchantId = UUID.fromString(id);
    MerchantResponse response = merchantService.findById(merchantId);

    return ResponseEntity.ok(response);
  }
}
