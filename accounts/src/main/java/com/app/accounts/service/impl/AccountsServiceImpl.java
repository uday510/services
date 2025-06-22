package com.app.accounts.service.impl;

import com.app.accounts.constants.AccountsConstants;
import com.app.accounts.dto.AccountsDto;
import com.app.accounts.dto.AccountsMessageDto;
import com.app.accounts.dto.CustomerDto;
import com.app.accounts.entity.Accounts;
import com.app.accounts.entity.Customer;
import com.app.accounts.exception.CustomerAlreadyExistsException;
import com.app.accounts.exception.ResourceNotFoundException;
import com.app.accounts.mapper.AccountMapper;
import com.app.accounts.mapper.CustomerMapper;
import com.app.accounts.repository.AccountsRepository;
import com.app.accounts.repository.CustomerRepository;
import com.app.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final StreamBridge streamBridge;
    private final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

    @Override
    public void createAccount(CustomerDto customerDto) {
        customerRepository.findByMobileNumber(customerDto.getMobileNumber()).ifPresent(customer -> {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists with this mobile number "
                            + customerDto.getMobileNumber());
        });

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        Accounts account = createNewAccount(savedCustomer);
        Accounts createdAccount = accountsRepository.save(account);

        sendCommunication(createdAccount, savedCustomer);
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts account = new Accounts();
        account.setCustomerId(customer.getCustomerId());
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
        account.setCreatedBy("Anonymous");
        account.setCreatedAt(LocalDateTime.now());

        return account;
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMessageDto = new AccountsMessageDto(account.getAccountNumber(),
                customer.getName(), customer.getEmail(), customer.getMobileNumber());

        logger.info("Sending Communication request for the details, {}", accountsMessageDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMessageDto);
        logger.info("Communication Result, {}", result);
    }

    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));


        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "accountNumber",
                        customer.getCustomerId().toString()));


        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    public void updateAccount(CustomerDto customerDto) {
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto == null) {
            throw new ResourceNotFoundException("Account", "Account DTO", null);
        }

        Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Account Number",
                        accountsDto.getAccountNumber().toString()));

        AccountMapper.mapToAccounts(accountsDto, accounts);
        accounts = accountsRepository.save(accounts);

        long customerId = accounts.getCustomerId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer Id",
                        String.valueOf(customerId)));

        CustomerMapper.mapToCustomer(customerDto, customer);
        customerRepository.save(customer);
    }

    private long generateAccountNumber() {
        return 1_000_000_000L + new Random().nextInt(900_000_000);
    }

    public void deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

    }
}
