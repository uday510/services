package com.app.accounts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't serialize null fields
@Schema(
        name = "SuccessResponse",
        description = "Schema to hold successful response information"
)
public class SuccessResponseDto<T> extends BaseResponseDto {

    @Schema(
            description = "Data to hold response payload"
    )
    private final T data;
    @Schema(
            description = "Message to hold response message information"
    )
    private final String message;

    @Builder(builderMethodName = "successBuilder", buildMethodName = "create")
    private SuccessResponseDto(String apiPath, HttpStatus status, OffsetDateTime timestamp, T data, String message) {
        super(apiPath, status.value(), timestamp);

        if (!status.is2xxSuccessful()) {
            throw new IllegalArgumentException("HttpStatus must be 2xx for success response.");
        }

        this.data = data;
        this.message = message;
    }

    public static <T> SuccessResponseDto<T> of(String apiPath, HttpStatus status, String message, T data) {
        return SuccessResponseDto.<T>successBuilder()
                .apiPath(apiPath)
                .status(status)
                .timestamp(OffsetDateTime.now())
                .message(message)
                .data(data)
                .create();
    }

}