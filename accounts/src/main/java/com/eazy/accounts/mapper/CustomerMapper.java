package com.eazy.accounts.mapper;

import com.eazy.accounts.dto.CustomerDto;
import com.eazy.accounts.entity.Accounts;
import com.eazy.accounts.entity.Customer;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer, Accounts acc) {
        CustomerDto customerDto = new CustomerDto(
                customer.getName(),
                customer.getEmail(),
                customer.getMobileNumber(),
                AccountsMapper.mapToAccountsDto(acc));
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.name());// Note: .accountNumber(), not .getAccountNumber()
        customer.setEmail(customerDto.email());
        customer.setMobileNumber(customerDto.mobileNumber());
        return customer;
    }

}