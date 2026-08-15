package com.example.demo.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class EnvConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "jwt.secret", () -> "9f7c2a1e8b4d6f3a0c5e9d2b7a4f1c8e6d3b0a9f5e2c7d4b8a1f6e3c9d5b2a7");
    registry.add("jwt.expiration-days", () -> "1");
  }
}
