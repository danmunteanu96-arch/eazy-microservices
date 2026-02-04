package com.eazy.loans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record LoansDto(
                @NotEmpty(message = "Mobile Number can not be a null or empty") @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile Number must be 7 digits")  String mobileNumber,

                @NotEmpty(message = "Loan Number can not be a null or empty") @Pattern(regexp = "(^$|[0-9]{12})", message = "LoanNumber must be 12 digits")  String loanNumber,

                @NotEmpty(message = "LoanType can not be a null or empty") String loanType,

                @Positive(message = "Total loan amount should be greater than zero") int totalLoan,

                @PositiveOrZero(message = "Total loan amount paid should be equal or greater than zero") int amountPaid,

                @PositiveOrZero(message = "Total outstanding amount should be equal or greater than zero") int outstandingAmount

) {

}