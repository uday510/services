package com.app.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.accounts.dto.LoansDto;
import com.app.accounts.dto.SuccessResponseDto;

@FeignClient("loans") 
public interface LoansFiegnClient {
  
  @GetMapping(value = "/api/loans", consumes = "application/json")
  public ResponseEntity<SuccessResponseDto<LoansDto>> fetchLoanDetails(@RequestParam String mobileNumber);

}
