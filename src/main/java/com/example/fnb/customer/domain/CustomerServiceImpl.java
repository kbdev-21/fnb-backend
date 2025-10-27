package com.example.fnb.customer.domain;

import com.example.fnb.customer.CustomerService;
import com.example.fnb.customer.dto.CreateCustomerDto;
import com.example.fnb.customer.dto.CustomerDto;
import com.example.fnb.shared.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto createDto) {
        var normalizedName = StringUtil.normalizeVietnamese(createDto.getFirstName() + " " + createDto.getLastName());

        var newCustomer = new Customer();
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setPhoneNum(createDto.getPhoneNum());
        newCustomer.setEmail(createDto.getEmail());
        newCustomer.setFirstName(createDto.getFirstName());
        newCustomer.setLastName(createDto.getLastName());
        newCustomer.setNormalizedName(normalizedName);
        newCustomer.setUserId(newCustomer.getId());
        newCustomer.setMoneySpent(BigDecimal.ZERO);
        newCustomer.setLoyaltyPoints(0);
        newCustomer.setCreatedAt(Instant.now());

        var savedCustomer = customerRepository.save(newCustomer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Override
    public List<CustomerDto> getCustomers() {
        var customers = customerRepository.findAll();
        return customers.stream()
            .map(customer -> modelMapper.map(customer, CustomerDto.class))
            .toList();
    }
}
