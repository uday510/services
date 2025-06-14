package com.app.cards.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(
        name = "BaseResponse",
        description = "Schema for Base Response"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponseDto {

    @Schema(description = "Path of the API")
    private String apiPath;

    @Schema(description = "HTTP status code")
    private int statusCode;

    @Schema(description = "Timestamp of the response")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;

    protected BaseResponseDto() {
        // Default constructor for Jackson
    }

    protected BaseResponseDto(String apiPath, int statusCode, OffsetDateTime timestamp) {
        this.apiPath = apiPath;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    // Getters
    public String getApiPath() {
        return apiPath;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}