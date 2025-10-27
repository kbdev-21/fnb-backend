package com.example.fnb.customer.domain;

import com.example.fnb.auth.event.RegisterSuccessEvent;
import com.example.fnb.customer.CustomerService;
import com.example.fnb.customer.dto.CreateCustomerDto;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository; /* TODO: write method in service and use it instead */

    public CustomerEventListener(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @EventListener
    @Async
    public void handleRegisterSuccessEvent(RegisterSuccessEvent event) {
        var newUser = event.getUser();
        var customer = customerRepository.findByPhoneNum(newUser.getPhoneNum());
        /* TODO: more */
        if(customer.isEmpty()) {
            customerService.createCustomer(new CreateCustomerDto(
                newUser.getPhoneNum(),
                newUser.getEmail(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getId()
            ));
        }
    }
}
