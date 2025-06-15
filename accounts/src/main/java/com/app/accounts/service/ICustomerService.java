package com.app.accounts.service;

import com.app.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

  CustomerDetailsDto fetchCustomerDetailsDto(String correlationId, String mobileNumber) throws Exception;

}
