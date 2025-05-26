package com.app.accounts.controller;

import com.app.accounts.dto.CustomerDto;
import com.app.accounts.dto.SuccessResponseDto;
import com.app.accounts.service.IAccountInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class AccountsController {

    private IAccountInterface iAccountInterface;

    @PostMapping(path = {"", "/"})
    public ResponseEntity<SuccessResponseDto<String>> createAccount(
            @Valid
            @RequestBody CustomerDto customerDto,
            HttpServletRequest request
    ) {
        iAccountInterface.createAccount(customerDto);

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.CREATED,
                HttpStatus.CREATED.getReasonPhrase(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = {"", "/"} )
    public ResponseEntity<SuccessResponseDto<CustomerDto>> fetchAccount(@RequestParam
                                                                            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                            String mobileNumber, HttpServletRequest request) {

        CustomerDto customerDto = iAccountInterface.fetchAccount(mobileNumber);

        SuccessResponseDto<CustomerDto> response = SuccessResponseDto.of(
          request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                customerDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = {"", "/"})
    public ResponseEntity<SuccessResponseDto<Object>> updateAccount(@Valid @RequestBody CustomerDto dto, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "updated");
        data.put("success", true);

        iAccountInterface.updateAccount(dto);
        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(path = {"", "/"})
    public ResponseEntity<SuccessResponseDto<Object>> deleteAccount (@RequestParam
                                                                         @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                         String mobileNumber,
                                                                     HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "deleted");
        data.put("success", true);

        iAccountInterface.deleteAccount(mobileNumber);

        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
