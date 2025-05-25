package com.app.accounts.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class CustomerDto {

    private String name;

    private String email;

    private String mobileNumber;

    private AccountsDto accountsDto;

}
