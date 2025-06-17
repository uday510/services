package com.app.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.accounts.dto.LoansDto;
import com.app.accounts.dto.SuccessResponseDto;

@FeignClient(name="loans", fallback = LoansFallback.class) 
public interface LoansFeignClient {
  
  @GetMapping(value = "/api/loans", consumes = "application/json")
  ResponseEntity<SuccessResponseDto<LoansDto>> fetchLoanDetails(@RequestHeader("app-correlation-id") String correlationId, @RequestParam String mobileNumber);

}
