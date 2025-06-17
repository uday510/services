package com.app.accounts.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.accounts.dto.AccountsDto;
import com.app.accounts.dto.CardsDto;
import com.app.accounts.dto.CustomerDetailsDto;
import com.app.accounts.dto.LoansDto;
import com.app.accounts.dto.SuccessResponseDto;
import com.app.accounts.entity.Accounts;
import com.app.accounts.entity.Customer;
import com.app.accounts.exception.ResourceNotFoundException;
import com.app.accounts.mapper.AccountMapper;
import com.app.accounts.mapper.CustomerMapper;
import com.app.accounts.repository.AccountsRepository;
import com.app.accounts.repository.CustomerRepository;
import com.app.accounts.service.ICustomerService;
import com.app.accounts.service.client.CardsFeignClient;
import com.app.accounts.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

  private final AccountsRepository accountsRepository;
  private final CustomerRepository customerRepository;
  private final CardsFeignClient cardsFeignClient;
  private final LoansFeignClient loansFeignClient;

  @Override
  public CustomerDetailsDto fetchCustomerDetailsDto(String correlationId, String mobileNumber) throws Exception {
    Customer customer = customerRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

    CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
    customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

    try {
      ResponseEntity<SuccessResponseDto<LoansDto>> loanResponse = loansFeignClient.fetchLoanDetails(correlationId,
          mobileNumber);
      if (loanResponse != null && loanResponse.getBody() != null) {
        customerDetailsDto.setLoansDto(loanResponse.getBody().getData());
      } else {
        System.out.println("Loan service fallback or empty response triggered.");
      }
    } catch (Exception ex) {
      System.err.println("Error calling loan service: " + ex.getMessage());
    }

    try {
      ResponseEntity<SuccessResponseDto<CardsDto>> cardResponse = cardsFeignClient.fetchCardDetails(correlationId,
          mobileNumber);
      if (cardResponse != null && cardResponse.getBody() != null) {
        customerDetailsDto.setCardsDto(cardResponse.getBody().getData());
      } else {
        System.out.println("Card service fallback or empty response triggered.");
      }
    } catch (Exception ex) {
      System.err.println("Error calling card service: " + ex.getMessage());
    }

    return customerDetailsDto;
  }


}
