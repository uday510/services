package com.app.loans.controller;

import com.app.loans.dto.ErrorResponseDto;
import com.app.loans.dto.LoansContactInfoDto;
import com.app.loans.dto.LoansDto;
import com.app.loans.dto.SuccessResponseDto;
import com.app.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Uday Teja
 */

@Tag(
        name = "CRUD REST APIs for Cards in Loans Service",
        description = "CRUD REST APIs in Loans Service to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api/loans", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class LoansController {

    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

    @Value("${build.version}")
    private String buildVersion;

    private final ILoansService iLoansService;

    private final Environment environment;

    private final LoansContactInfoDto loansContactInfoDto;

    public LoansController(
            ILoansService iLoansService,
            Environment environment,
            LoansContactInfoDto loansContactInfoDto
    ) {
        this.iLoansService = iLoansService;
        this.environment = environment;
        this.loansContactInfoDto = loansContactInfoDto;
    }

    @Operation(
            summary = "Create Loan REST API",
            description = "REST API to create new Loan inside Loans Service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping
    public ResponseEntity<SuccessResponseDto<String>> createLoan(@Valid @RequestParam
                                                                 @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                 String mobileNumber, HttpServletRequest request) {
        iLoansService.createLoan(mobileNumber);

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.CREATED,
                HttpStatus.CREATED.getReasonPhrase(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Fetch Loan Details REST API",
            description = "REST API to fetch loan details based on a mobile number"
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

    @GetMapping
    public ResponseEntity<SuccessResponseDto<LoansDto>> fetchLoanDetails(
                                                     @RequestHeader("app-correlation-id") String correlationId,
                                                     @RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber, HttpServletRequest request) {


        System.out.println ("Correlation : " + correlationId);
        logger.debug("Correlation Id Found: {}", correlationId);

        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);

        SuccessResponseDto<LoansDto> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                loansDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Update Loan Details REST API",
            description = "REST API to update card details based on a card number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping
    public ResponseEntity<SuccessResponseDto<Object>> updateLoanDetails(@Valid @RequestBody LoansDto loansDto, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "updated");
        data.put("success", true);

        iLoansService.updateLoad(loansDto);
        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Delete Loan REST API",
            description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<SuccessResponseDto<Object>> deleteLoanAccount(@RequestParam
                                                                     @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                     String mobileNumber,
                                                                     HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "deleted");
        data.put("success", true);

        iLoansService.deleteLoan(mobileNumber);

        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Fetch Build Info REST API",
            description = "REST API to fetch Build information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/build-info")
    public ResponseEntity<SuccessResponseDto<String>> getBuildInfo(HttpServletRequest request) {

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                buildVersion
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Fetch JAVA Version REST API",
            description = "REST API to fetch Java version information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/java-version")
    public ResponseEntity<SuccessResponseDto<String>> getJavaVersion(HttpServletRequest request) {

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                environment.getProperty("JAVA_HOME")
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Fetch Maven Version REST API",
            description = "REST API to fetch Maven version information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/maven-home")
    public ResponseEntity<SuccessResponseDto<String>> getMavenHome(HttpServletRequest request) {

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                environment.getProperty("MAVEN_HOME")
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Fetch Contact Info REST API",
            description = "REST API to fetch Contact info information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/contact")
    public ResponseEntity<SuccessResponseDto<LoansContactInfoDto>> getContactInfo(HttpServletRequest request) {

        SuccessResponseDto<LoansContactInfoDto> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                loansContactInfoDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
