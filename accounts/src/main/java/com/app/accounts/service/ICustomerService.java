package com.app.accounts.service;

import com.app.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

  CustomerDetailsDto fetcCustomerDetailsDto(String mobileNumber) throws Exception;

}
