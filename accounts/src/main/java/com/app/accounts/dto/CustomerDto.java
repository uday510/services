package com.app.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDto {

    @Schema(
            description = "Name of the customer",
            example = "Uday Teja"
    )
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "The length of the name should be between 5 and 30")
    private String name;

    @Schema(
            description = "Email of the customer",
            example = "uday@udayteja.com"
    )
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Invalid email address")
    private String email;

    @Schema(
            description = "Mobile Number of the customer",
            example = "+91 9876543210"
    )
    @NotEmpty(message = "Mobile cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account details of the customer"
    )
    private AccountsDto accountsDto;
}
