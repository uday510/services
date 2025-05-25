package com.app.accounts.controller;

import com.app.accounts.dto.CustomerDto;
import com.app.accounts.dto.SuccessResponseDto;
import com.app.accounts.service.impl.AccountsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AccountsController {

    private AccountsServiceImpl accountsServiceImpl;

    @PostMapping(path = {"", "/"})
    public ResponseEntity<SuccessResponseDto<String>> createAccount(
            @RequestBody CustomerDto customerDto,
            HttpServletRequest request
    ) {

        accountsServiceImpl.createAccount(customerDto);

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.CREATED,
                HttpStatus.CREATED.getReasonPhrase(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = {"", "/"} )
    public ResponseEntity<SuccessResponseDto<CustomerDto>> fetchAccountDetails (@RequestParam String mobileNumber, HttpServletRequest request) {
        CustomerDto customerDto = accountsServiceImpl.fetchAccount(mobileNumber);

        SuccessResponseDto<CustomerDto> response = SuccessResponseDto.of(
          request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                customerDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
