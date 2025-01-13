package com.github.caac.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
public class CustomerCrudService {
    
    private CustomerRepository customerRepository;

    public CustomerCrudService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        Optional<Customer> person = customerRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    @Validated
    public Customer createPerson(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    @Validated
    public Customer createCustomerWithAddress(Customer customer, List<Address> addresses) {
        customer.setAddresses(addresses);
        addresses.forEach(address -> address.setPerson(customer));

        return customerRepository.save(customer);
    }
}
