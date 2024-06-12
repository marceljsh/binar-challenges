package com.marceljsh.binarfud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

  @GetMapping(
    value = "/hello",
    produces = "application/json"
  )
  public ResponseEntity<Map<String, String>> ping() {
    return ResponseEntity.ok(Map.of("message", "wattup cuh"));
  }
}
