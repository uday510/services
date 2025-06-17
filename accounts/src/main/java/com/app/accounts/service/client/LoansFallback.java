package com.app.accounts.service.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.app.accounts.dto.LoansDto;
import com.app.accounts.dto.SuccessResponseDto;

@Component
public class LoansFallback implements LoansFeignClient {

  @Override
  public ResponseEntity<SuccessResponseDto<LoansDto>> fetchLoanDetails(String correlationId, String mobileNumber) {
    LoansDto fallbackDto = new LoansDto();

    SuccessResponseDto<LoansDto> response = SuccessResponseDto.of(
        "/api/loans",
        HttpStatus.SERVICE_UNAVAILABLE,
        "Fallback: Loans service is currently unavailable",
        fallbackDto);

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }
}