package com.eazy.accounts.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDto(
        @NotEmpty(message = "Name can not be empty.") @Size(min = 2, message = "Name must contain at least 2 char.") String name,
        @NotEmpty(message = "Email must not be empty.") @Email(message = "You must enter a valid email") String email,
        @NotNull(message = "Account number can not be null") String mobileNumber,

        @Valid
        AccountsDto accountsDto) {

}
