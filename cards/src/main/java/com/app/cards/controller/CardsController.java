package com.app.cards.controller;

import com.app.cards.dto.CardsDto;
import com.app.cards.dto.ErrorResponseDto;
import com.app.cards.dto.SuccessResponseDto;
import com.app.cards.service.ICardsService;
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

/**
 * @author Uday Teja
 */

@Tag(
        name = "CRUD REST APIs for Cards in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api/cards", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class CardsController {

    private ICardsService iCardsService;

    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card inside EazyBank"
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
    public ResponseEntity<SuccessResponseDto<String>> createCard(@Valid @RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber, HttpServletRequest request) {
        iCardsService.createCard(mobileNumber);

        SuccessResponseDto<String> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.CREATED,
                HttpStatus.CREATED.getReasonPhrase(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch card details based on a mobile number"
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
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber, HttpServletRequest request) {

        CardsDto cardsDto = iCardsService.fetchCard(mobileNumber);

        SuccessResponseDto<CardsDto> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                cardsDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @Operation(
            summary = "Update Card Details REST API",
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
    public ResponseEntity<SuccessResponseDto<Object>> updateCardDetails(@Valid @RequestBody CardsDto cardsDto, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "updated");
        data.put("success", true);

        iCardsService.updateCard(cardsDto);
        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Delete Card Details REST API",
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
    public ResponseEntity<SuccessResponseDto<Object>> deleteAccount (@RequestParam
                                                                     @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                     String mobileNumber,
                                                                     HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "deleted");
        data.put("success", true);

        iCardsService.deleteCard(mobileNumber);

        SuccessResponseDto<Object> response = SuccessResponseDto.of(
                request.getRequestURI(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase(),
                data
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}