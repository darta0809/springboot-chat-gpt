package com.example.springbootchatgpt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final Service service;

  @GetMapping("test")
  void test() {
    service.test();
  }
}
