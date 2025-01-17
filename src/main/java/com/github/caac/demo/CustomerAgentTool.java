package com.github.caac.demo;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import dev.langchain4j.agent.tool.Tool;

public class CustomerAgentTool {

    private final CustomerRepository customerRepository;

    private AddressRepository addressRepository;

    public CustomerAgentTool(CustomerRepository service, AddressRepository addressRepository) {
        this.customerRepository = service;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    @Tool("""
            Creates a new customer with one or more addresses.
            """)
    public Customer createCustomerWithAddress(Customer customer, List<Address> addresses) {
        customer.setAddresses(addresses);
        addresses.forEach(address -> address.setPerson(customer));

        return customerRepository.save(customer);
    }

}
