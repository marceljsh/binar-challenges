package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.MerchantAddRequest;
import com.marceljsh.binfood.payload.request.MerchantSearchRequest;
import com.marceljsh.binfood.payload.request.MerchantUpdateRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.MerchantResponse;
import com.marceljsh.binfood.payload.response.PagingResponse;
import com.marceljsh.binfood.service.spec.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

  private final MerchantService merchantService;

  @Autowired
  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<MerchantResponse> add(User user, @RequestBody MerchantAddRequest request) {
    MerchantResponse response =  merchantService.save(request);
    return ApiResponse.<MerchantResponse>builder()
        .data(response)
        .build();
  }

  @GetMapping(
    path = "/{merchant-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public MerchantResponse get(@PathVariable("merchant-id") String id) {
    return merchantService.findById(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<List<MerchantResponse>> search(User user, @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "location", required = false) String location,
      @RequestParam(value = "open", required = false) Boolean open,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {

    MerchantSearchRequest request = MerchantSearchRequest.builder()
        .name(name)
        .location(location)
        .open(open)
        .page(page)
        .size(size)
        .build();

    Page<MerchantResponse> responses = merchantService.get(request);

    return ApiResponse.<List<MerchantResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(responses.getNumber())
            .totalPages(responses.getTotalPages())
            .size(responses.getSize())
            .build())
        .build();
  }

  @GetMapping(path = "/all-open", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<List<MerchantResponse>> getAllOpen(User user, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
    Page<MerchantResponse> responses = merchantService.getAllOpen(page, size);

    return ApiResponse.<List<MerchantResponse>>builder()
        .data(responses.getContent())
        .paging(PagingResponse.builder()
            .currentPage(responses.getNumber())
            .totalPages(responses.getTotalPages())
            .size(responses.getSize())
            .build())
        .build();
  }

  @PutMapping(path = "/{merchant-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MerchantResponse update(User user, @RequestBody MerchantUpdateRequest request, @PathVariable("merchant-id") String id) {
    request.setId(id);
    return merchantService.update(request);
  }

  @PatchMapping(path = "/{merchant-id}/open", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> open(User user, @PathVariable("merchant-id") String id) {
    merchantService.updateStatus(id, true);

    return ApiResponse.<String>builder().data("OK").build();
  }

  @PatchMapping(path = "/{merchant-id}/close", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> close(User user, @PathVariable("merchant-id") String id) {
    merchantService.updateStatus(id, false);

    return ApiResponse.<String>builder().data("OK").build();
  }

  @DeleteMapping(path = "/{merchant-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> remove(User user, @PathVariable("merchant-id") String id) {
    merchantService.remove(id);

    return ApiResponse.<String>builder().data("OK").build();
  }
}
