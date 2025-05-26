package com.app.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
public class AccountsDto {

    @Schema(
            description = "Account Number of the customer",
            example = "+91 1234567890"
    )
    @NotEmpty(message = "Account Number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account Type of the customer",
            example = "SAVINGS"
    )
    @NotEmpty(message = "Account Type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "Branch Address of the customer",
            example = "Vijayawada, Andhra Pradesh"
    )
    @NotEmpty(message = "Branch Address cannot be null or empty")
    private String branchAddress;

}
