package com.github.caac.demo;

import dev.langchain4j.agent.tool.Tool;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerAgentTool {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgentTool.class);

    private final CustomerRepository customerRepository;

    public CustomerAgentTool(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Validated
    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the customer id.
            """)
    public Optional<Customer> getCustomerById(@NotNull Long customerId) {
        logger.info("getCustomerById parameter: " + customerId);
        return customerRepository.findById(customerId);
    }

    @Validated
    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public Customer getCustomerByEmail(@NotNull String email) {
        logger.info("getCustomerByEmail parameter: " + email);
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
