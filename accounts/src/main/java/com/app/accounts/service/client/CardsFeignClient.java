package com.app.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.accounts.dto.CardsDto;
import com.app.accounts.dto.SuccessResponseDto;

@FeignClient("cards")
public interface CardsFeignClient {

  @GetMapping(value = "/api/cards", consumes = "application/json")
  public ResponseEntity<SuccessResponseDto<CardsDto>> fetchCardDetails(@RequestHeader("app-correlation-id") String correlationId, @RequestParam String mobileNumber);
  
} 
