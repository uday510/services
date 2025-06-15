package com.app.gatewayserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/accounts")
    public ResponseEntity<Map<String, Object>> accountsFallback() {
        return buildFallback("Accounts service");
    }

    @GetMapping("/cards")
    public ResponseEntity<Map<String, Object>> cardsFallback() {
        return buildFallback("Cards service");
    }

    @GetMapping("/loans")
    public ResponseEntity<Map<String, Object>> loansFallback() {
        return buildFallback("Loans service");
    }

    private ResponseEntity<Map<String, Object>> buildFallback(String serviceName) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                Map.of(
                        "service", serviceName,
                        "timestamp", Instant.now().toString(),
                        "message", serviceName + " is currently unavailable. Please try again later."
                )
        );
    }
}