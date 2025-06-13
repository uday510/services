package com.app.accounts.service.impl;

import org.apache.commons.lang.UnhandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.accounts.dto.AccountsDto;
import com.app.accounts.dto.CardsDto;
import com.app.accounts.dto.CustomerDetailsDto;
import com.app.accounts.dto.CustomerDto;
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
import com.app.accounts.service.client.LoansFiegnClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService  {

  private final AccountsRepository accountsRepository;
  private final CustomerRepository customerRepository;
  private final CardsFeignClient cardsFeignClient;
  private final LoansFiegnClient loansFeignClient;

  @SuppressWarnings("null")
  @Override
  public CustomerDetailsDto fetcCustomerDetailsDto(String mobileNumber) throws Exception {
    Customer customer = customerRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Accounts", "accountNumber", customer.getCustomerId().toString()));

    CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,
        new CustomerDetailsDto());

    customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));
    ResponseEntity<SuccessResponseDto<LoansDto>> loanDetails = loansFeignClient.fetchLoanDetails(mobileNumber);
    if (loanDetails == null || loanDetails.getBody() == null) throw new Exception("LOAN DETAILS ::: ");
    
    customerDetailsDto.setLoansDto(loanDetails.getBody().getData());

    ResponseEntity<SuccessResponseDto<CardsDto>> cardDetails = cardsFeignClient.fetchCardDetails(mobileNumber);
    if (cardDetails == null || cardDetails.getBody() == null) throw new Exception("CARDS DETAILS ::: ");

    customerDetailsDto.setCardsDto(cardDetails.getBody().getData());
    
    return customerDetailsDto;
  }
  
}
