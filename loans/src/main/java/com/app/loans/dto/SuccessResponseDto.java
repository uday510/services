package com.app.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Schema(
        name = "SuccessResponse",
        description = "Schema for successful response"
)
public class SuccessResponseDto<T> extends BaseResponseDto {

    @Schema(description = "Payload data")
    private T data;

    @Schema(description = "Success message")
    private String message;

    public SuccessResponseDto() {
        super();
    }

    public SuccessResponseDto(String apiPath, HttpStatus status, OffsetDateTime timestamp, T data, String message) {
        super(apiPath, status.value(), timestamp);

        if (!status.is2xxSuccessful()) {
            throw new IllegalArgumentException("HttpStatus must be 2xx for success response.");
        }

        this.data = data;
        this.message = message;
    }

    public static <T> SuccessResponseDto<T> of(String apiPath, HttpStatus status, String message, T data) {
        return new SuccessResponseDto<>(apiPath, status, OffsetDateTime.now(), data, message);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}