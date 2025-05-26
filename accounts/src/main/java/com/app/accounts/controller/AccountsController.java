package com.app.accounts.controller;

import com.app.accounts.dto.CustomerDto;
import com.app.accounts.dto.ErrorResponseDto;
import com.app.accounts.dto.SuccessResponseDto;
import com.app.accounts.service.IAccountInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "CRUD REST APIs for Accounts",
        description = "CRUD APIs: CREATE, UPDATE, FETCH AND DELETE"
)
@RestController
@RequestMapping(value = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class AccountsController {

    private IAccountInterface iAccountInterface;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer & Account inside Accounts Service"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
//    @PostMapping(path = {"", "/"})
    @PostMapping
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

    @Operation(
            summary = "Fetch Account REST API",
            description = "REST API to fetch Customer & Account inside Accounts Service"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
//    @GetMapping(path = {"", "/"} )
    @GetMapping
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

    @Operation(
            summary = "Update Account REST API",
            description = "REST API to update Customer & Account inside Accounts Service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                  responseCode = "500",
                  description = "HTTP Status Internal Server Error",
                  content = @Content(
                          schema = @Schema(implementation = ErrorResponseDto.class)
                  )
            )
    })
//    @PutMapping(path = {"", "/"})
    @PutMapping
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

    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer & Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    })
//    @DeleteMapping(path = {"", "/"})
    @DeleteMapping
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
