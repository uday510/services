package com.app.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.accounts.dto.CustomerDetailsDto;
import com.app.accounts.dto.SuccessResponseDto;
import com.app.accounts.service.ICustomerService;

@Tag(name = "CRUD REST APIs for Accounts", description = "CRUD APIs: CREATE, UPDATE, FETCH AND DELETE")
@RestController
@RequestMapping(value = "/api/customer", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@AllArgsConstructor
public class CustomerController {

        private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

        private final ICustomerService iCustomerService;

        @Operation(summary = "Fetch Customer Details REST API", description = "REST API to fetch Customer details based on mobile number")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")

        @GetMapping
        public ResponseEntity<SuccessResponseDto<CustomerDetailsDto>> fetchCustomerDetails(
                        @RequestHeader("app-correlation-id") String correlationId,
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber,
                        HttpServletRequest request) throws Exception {

                Thread thread = new Thread(() -> {
                        try {
                                logger.debug("sleeping...");
                                Thread.sleep(10_000);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                });
                thread.start();
                // thread.join();
                

                CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetailsDto(correlationId,
                                mobileNumber);
                SuccessResponseDto<CustomerDetailsDto> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                customerDetailsDto);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

}