package com.github.caac.demo;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.langchain4j.agent.tool.Tool;

@Component
public class CustomerAgentTool {

    private static Logger logger = LoggerFactory.getLogger(CustomerAgentTool.class);

    private final CustomerRepository customerRepository;

    public CustomerAgentTool(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the customer id.
            """)
    public Optional<Customer> getCustomer(Long customerId) {
        logger.info("getCustomerById: " + customerId);
        return customerRepository.findById(customerId);
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public Customer getCustomer(String email) {
        logger.info("getCustomerByEmail: " + email);
        return customerRepository.findByEmail(email);
    }

    @Transactional
    @Tool("""
            Creates a new customer with one or more addresses.
            """)
    public Customer createCustomerWithAddress(Customer customer, List<Address> addresses) {
        logger.info("createCustomerWithAddress");
        customer.setAddresses(addresses);
        addresses.forEach(address -> address.setCustomer(customer));

        return customerRepository.save(customer);
    }

    @Transactional
    @Tool("""
            Creates a new customer.
            """)
    public Customer createCustomer(Customer customer) {
        logger.info("createCustomer");
        return customerRepository.save(customer);
    }

}
