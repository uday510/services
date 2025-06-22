package com.app.accounts.service;

import com.app.accounts.dto.CustomerDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    void updateAccount(CustomerDto customerDto);

    void deleteAccount(String mobileNumber);

    boolean updateCommunicationStatus(Long accountNumber);

}
