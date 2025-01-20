package com.github.caac.demo;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class CustomerAgentTool {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgentTool.class);

    private final CustomerRepository customerRepository;

    public CustomerAgentTool(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves all customers  and returns a list of customers.
            """)
    public String getAllCustomers() {
        logger.info("getAllCustomers with no parameter");
        List<Customer> all = customerRepository.findAll();
        String allString = all.toString();
        return allString;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the customer id.
            """)
    public String getCustomerById(@NotNull(message = "Customer ID cannot be null.") Long customerId) {
        logger.info("getCustomerById parameter: " + customerId);
        return customerRepository.findById(customerId).get().getName();
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public String getCustomerByEmail(@NotNull(message = "Customer email cannot be null.")  String email) {
        logger.info("getCustomerByEmail parameter: " + email);
        return customerRepository.findByEmail(email).get().getName();
    }

    @Transactional
    @Tool("""
            Creates a new customer with a list of addresses and returns the 
            created customer.
            """)
    public Customer createCustomerWithAddress(@NotNull @P("The customer object") Customer customer,
                                              @NotNull @P("A list of address objects") List<Address> addresses) {
        logger.info("createCustomerWithAddress");
        customer.setAddresses(addresses);
        addresses.forEach(address -> address.setCustomer(customer));

        return customerRepository.save(customer);
    }

    @Transactional
    @Tool("""
            Creates a new customer without addresses and returns the created customer.
            """)
    public Customer createCustomer(@NotNull @P("The customer object")  Customer customer) {
        logger.info("createCustomer parameter: " + customer);
        return customerRepository.save(customer);
    }

}
