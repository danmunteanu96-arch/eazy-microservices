package com.eazy.accounts.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

import com.eazy.accounts.constants.AccountsConstants;
import com.eazy.accounts.dto.AccountsConfigPropDto;
import com.eazy.accounts.dto.CustomerDto;
import com.eazy.accounts.dto.ResponseDto;
import com.eazy.accounts.service.IAccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping(path = "api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class AccountController {
        @Value("${application.version}")
        private String buildVersion;

        private final Environment env;

        private final IAccountService accountService;
        private final AccountsConfigPropDto accountsConfigPropDto;

        public AccountController(IAccountService accountService, Environment env,
                        AccountsConfigPropDto accountsConfigPropDto) {
                this.accountService = accountService;
                this.env = env;
                this.accountsConfigPropDto = accountsConfigPropDto;
        }

        @GetMapping("/fetch")
        public ResponseEntity<CustomerDto> fetchAccount(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {
                CustomerDto c = accountService.fetchAccount(mobileNumber);
                return ResponseEntity.ok(c);
        }

        @PostMapping
        @RequestMapping(path = "/create")
        public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
                accountService.createAccount(customerDto);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
        }

        @PutMapping("/update")
        public ResponseEntity<ResponseDto> updateAccDetails(@Valid @RequestBody CustomerDto customerDto) {
                boolean isUpdated = accountService.updateAccount(customerDto);

                return isUpdated ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                AccountsConstants.MESSAGE_500,
                                                                HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }

        @DeleteMapping("/delete-account")
        public ResponseEntity<ResponseDto> deleteAcc(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {
                boolean isDeleted = accountService.deleteAcc(mobileNumber);

                return isDeleted ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                AccountsConstants.MESSAGE_500,
                                                                HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }

        @GetMapping("/build-info")
        public ResponseEntity<String> getBuildInfo() {
                return ResponseEntity.ok(this.buildVersion);
        }

        @GetMapping("/java-version")
        public ResponseEntity<?> getJavaVersion() {
                return ResponseEntity.ok(env.getProperty("JAVA_HOME"));
        }

        @GetMapping("/account-details")
        public ResponseEntity<AccountsConfigPropDto> getAccountConfig() {
                return ResponseEntity.ok(accountsConfigPropDto);
        }

}
