package com.eazy.loans.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazy.loans.constants.LoansConstants;
import com.eazy.loans.dto.LoansConfigPropDto;
import com.eazy.loans.dto.LoansDto;
import com.eazy.loans.dto.ResponseDto;
import com.eazy.loans.service.ILoansService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class LoansController {

        private ILoansService loanService;
        private LoansConfigPropDto loansConfigPropDto;

        @GetMapping()
        @RequestMapping(path = "/fetch")
        public ResponseEntity<LoansDto> fetchLoan(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {

                LoansDto loansDto = loanService.fetchLoan(mobileNumber);
                return ResponseEntity.status(HttpStatus.OK).body(loansDto);
        }

        @PostMapping
        @RequestMapping(path = "/create")
        public ResponseEntity<ResponseDto> createLoan(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {

                loanService.createLoan(mobileNumber);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
        }

        @PutMapping("/update")
        public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoansDto loansDto) {
                boolean isUpdated = loanService.updateLoan(loansDto);

                return isUpdated ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                LoansConstants.MESSAGE_417_UPDATE,
                                                                LoansConstants.STATUS_417));
        }

        @DeleteMapping("/delete-loan")
        public ResponseEntity<ResponseDto> deleteLoan(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {
                boolean isDeleted = loanService.deleteLoan(mobileNumber);

                return isDeleted ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                LoansConstants.MESSAGE_500,
                                                                HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }

        @GetMapping("/loans-details")
        public ResponseEntity<LoansConfigPropDto> getMethodName() {
                return ResponseEntity.ok(loansConfigPropDto);
        }

}
