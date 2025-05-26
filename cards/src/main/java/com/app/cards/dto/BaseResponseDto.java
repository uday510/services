package com.app.cards.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "BaseResponse",
        description = "Schema to BaseResponse"
)
public abstract class BaseResponseDto {

    @Schema(
            description = "Data to hold api-path"
    )
    private final String apiPath;
    @Schema(
            description = "Data to status-code"
    )
    private final int statusCode;

    @Schema(
            description = "Data to hold timestamp"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private final OffsetDateTime timestamp;
}