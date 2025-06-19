package com.app.accounts.controller;

import com.app.accounts.dto.*;
import com.app.accounts.service.IAccountService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "CRUD REST APIs for Accounts", description = "CRUD APIs: CREATE, UPDATE, FETCH AND DELETE")
@RestController
@RequestMapping(value = "/api/accounts", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class AccountsController {

        private final IAccountService iAccountService;

        private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

        @Value("${build.version}")
        private String buildVersion;

        private final Environment environment;

        private final AccountsContactInfoDto accountsContactInfoDto;

        public AccountsController(

                        IAccountService iAccountService,
                        Environment environment,
                        AccountsContactInfoDto accountsContactInfoDto) {

                this.iAccountService = iAccountService;
                this.environment = environment;
                this.accountsContactInfoDto = accountsContactInfoDto;
        }

        @Operation(summary = "Create Account REST API", description = "REST API to create new Customer & Account inside Accounts Service")
        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED")
        // @PostMapping(path = {"", "/"})
        @PostMapping
        public ResponseEntity<SuccessResponseDto<String>> createAccount(
                        @Valid @RequestBody CustomerDto customerDto,
                        HttpServletRequest request) {
                iAccountService.createAccount(customerDto);

                SuccessResponseDto<String> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.CREATED,
                                HttpStatus.CREATED.getReasonPhrase(),
                                null);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Fetch Account REST API", description = "REST API to fetch Customer & Account inside Accounts Service")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        // @GetMapping(path = {"", "/"} )
        @GetMapping
        public ResponseEntity<SuccessResponseDto<CustomerDto>> fetchAccount(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber,
                        HttpServletRequest request) {

                CustomerDto customerDto = iAccountService.fetchAccount(mobileNumber);
                
                // try {
                //         Thread.sleep(10_000);

                // } catch (InterruptedException e) {
                //         Thread.currentThread().interrupt();
                // }

                SuccessResponseDto<CustomerDto> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                customerDto);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Update Account REST API", description = "REST API to update Customer & Account inside Accounts Service")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        // @PutMapping(path = {"", "/"})
        @PutMapping
        public ResponseEntity<SuccessResponseDto<Object>> updateAccount(@Valid @RequestBody CustomerDto dto,
                        HttpServletRequest request) {
                Map<String, Object> data = new HashMap<>();
                data.put("status", "updated");
                data.put("success", true);

                iAccountService.updateAccount(dto);
                SuccessResponseDto<Object> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                data);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Delete Account & Customer Details REST API", description = "REST API to delete Customer & Account details based on a mobile number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error")
        })
        // @DeleteMapping(path = {"", "/"})
        @DeleteMapping
        public ResponseEntity<SuccessResponseDto<Object>> deleteAccount(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber,
                        HttpServletRequest request) {
                Map<String, Object> data = new HashMap<>();
                data.put("status", "deleted");
                data.put("success", true);

                iAccountService.deleteAccount(mobileNumber);

                SuccessResponseDto<Object> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                data);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
        @Operation(summary = "Fetch Build Info REST API", description = "REST API to fetch Build information")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/build-info")
        public ResponseEntity<SuccessResponseDto<String>> getBuildInfo(HttpServletRequest request) {

                // try {
                // Thread.sleep(10_000);
                // } catch (InterruptedException e) {
                // Thread.currentThread().interrupt();
                // System.out.println("Main thread was interrupted");
                // }

                // logger.info("retring...");

                throw new NullPointerException();

                // SuccessResponseDto<String> response = SuccessResponseDto.of(
                //                 request.getRequestURI(),
                //                 HttpStatus.OK,
                //                 HttpStatus.OK.getReasonPhrase(),
                //                 buildVersion);

                // return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        public ResponseEntity<SuccessResponseDto<String>> getBuildInfoFallback(HttpServletRequest request,
                        Throwable throwable) {

                SuccessResponseDto<String> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                HttpStatus.CONTINUE.toString());

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Fetch JAVA Version REST API", description = "REST API to fetch Java version information")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/java-version")
        public ResponseEntity<SuccessResponseDto<String>> getJavaVersion(HttpServletRequest request) {

                SuccessResponseDto<String> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                environment.getProperty("JAVA_HOME"));

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Fetch Maven Version REST API", description = "REST API to fetch Maven version information")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/maven-home")
        public ResponseEntity<SuccessResponseDto<String>> getMavenHome(HttpServletRequest request) {

                SuccessResponseDto<String> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                environment.getProperty("MAVEN_HOME"));

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Fetch Contact Info REST API", description = "REST API to fetch Contact info information")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/contact")
        public ResponseEntity<SuccessResponseDto<AccountsContactInfoDto>> getContactInfo(HttpServletRequest request) {

                SuccessResponseDto<AccountsContactInfoDto> response = SuccessResponseDto.of(
                                request.getRequestURI(),
                                HttpStatus.OK,
                                HttpStatus.OK.getReasonPhrase(),
                                accountsContactInfoDto);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

}
