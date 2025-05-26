package com.app.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@ToString(callSuper = true)
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold Error response information"
)
public class ErrorResponseDto extends BaseResponseDto {

    @Schema(
            description = "Data to hold error message"
    )
    private final String message;
    @Schema(
            description = "Data to hold error"
    )
    private final String error;

    @Builder(builderMethodName = "errorBuilder", buildMethodName = "create")
    private ErrorResponseDto(String apiPath, HttpStatus status, OffsetDateTime timestamp, String message, String error) {
        super(apiPath, status.value(), timestamp);
        if (!(status.is4xxClientError() || status.is5xxServerError())) {
            throw new IllegalArgumentException("HttpStatus must be 4xx or 5xx for error response.");
        }
        this.message = message;
        this.error = error;
    }

    public static ErrorResponseDto of(String apiPath, HttpStatus status, String message, String error) {
        return ErrorResponseDto.errorBuilder()
                .apiPath(apiPath)
                .status(status)
                .timestamp(OffsetDateTime.now())
                .message(message)
                .error(error)
                .create();
    }
}