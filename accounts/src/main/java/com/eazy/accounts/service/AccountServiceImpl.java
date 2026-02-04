package com.eazy.accounts.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.eazy.accounts.constants.AccountsConstants;
import com.eazy.accounts.dto.AccountsDto;
import com.eazy.accounts.dto.CustomerDto;
import com.eazy.accounts.entity.Accounts;
import com.eazy.accounts.entity.Customer;
import com.eazy.accounts.exceptions.CustomerAlreadyExists;
import com.eazy.accounts.exceptions.ResourceNotFound;
import com.eazy.accounts.mapper.AccountsMapper;
import com.eazy.accounts.mapper.CustomerMapper;
import com.eazy.accounts.repository.AccountRepository;
import com.eazy.accounts.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        customerRepository.findByMobileNumber(customerDto.mobileNumber())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExists(
                            "Customer already registered with mobile number " + customerDto.mobileNumber());
                });

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        // customer.setCreatedAt(LocalDateTime.now());
        // customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccountHelper(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer c = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFound("Customer", "mobileNumber", mobileNumber));

        Accounts acc = accountRepository.findByCustomerId(c.getCustomerId())
                .orElseThrow(
                        () -> new ResourceNotFound("Accounts", "customerId", c.getCustomerId().toString())

                );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(c, acc);

        return customerDto;
    }

    private Accounts createNewAccountHelper(Customer customer) {
        Accounts newAcc = new Accounts();
        newAcc.setCustomerId(customer.getCustomerId());
        long randomAccNumber = new Random().nextLong();
        newAcc.setAccountNumber(randomAccNumber);
        newAcc.setAccountType(AccountsConstants.SAVINGS);
        newAcc.setBranchAddress(AccountsConstants.ADDRESS);
        // newAcc.setCreatedAt(LocalDateTime.now());
        // newAcc.setCreatedBy("Anonymous");
        return newAcc;

    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.accountsDto();

        if (accountsDto != null) {
            Accounts accounts = accountRepository.findById(accountsDto.accountNumber())
                    .orElseThrow(
                            () -> new ResourceNotFound("Account", "accountNumber",
                                    accountsDto.accountNumber().toString()));

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFound("Customer", "CustomerID", customerId.toString()));

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean deleteAcc(String mobileNumber) {
        Customer c = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFound("Accounts", "mobileNumber", mobileNumber));
        accountRepository.deleteByCustomerId(c.getCustomerId());
        customerRepository.deleteById(c.getCustomerId());

        return true;
    }

}
