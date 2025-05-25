package com.app.accounts.service;

import com.app.accounts.dto.CustomerDto;

public interface IAccountInterface {
    /**
     *
     * @param customerDto - CustomerRequestDTO
     */

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

}
