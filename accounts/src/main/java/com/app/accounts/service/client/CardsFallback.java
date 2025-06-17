package com.app.accounts.service.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.app.accounts.dto.CardsDto;
import com.app.accounts.dto.SuccessResponseDto;

@Component
public class CardsFallback implements CardsFeignClient {

  @Override
  public ResponseEntity<SuccessResponseDto<CardsDto>> fetchCardDetails(String correlationId, String mobileNumber) {
    CardsDto fallbackDto = new CardsDto(); 

    SuccessResponseDto<CardsDto> response = SuccessResponseDto.of(
        "/api/cards",
        HttpStatus.SERVICE_UNAVAILABLE,
        "Fallback: Cards service is currently unavailable",
        fallbackDto);

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }
}