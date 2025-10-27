package com.example.fnb.customer;

import com.example.fnb.customer.dto.CreateCustomerDto;
import com.example.fnb.customer.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CreateCustomerDto createDto);
    List<CustomerDto> getCustomers();
}
