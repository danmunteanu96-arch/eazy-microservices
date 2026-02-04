package com.eazy.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountsDto(
                @NotNull(message = "Account number cannot be null") Long accountNumber,
                @NotEmpty(message = "AccountType cannot be null or empty") @Pattern(regexp = "(Savings|Checking|Current)", message = "AccountType must be either Savings or Checking") String accountType,
                @NotEmpty(message = "BranchAddress can not be a null or empty") String branchAddress) {

}
