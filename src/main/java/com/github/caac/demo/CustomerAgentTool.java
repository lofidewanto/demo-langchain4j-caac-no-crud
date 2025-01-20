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
    public CustomerDto getCustomerById(@NotNull(message = "Customer ID cannot be null.") Long arg0) {
        logger.info("getCustomerById parameter: " + arg0);
        var customer = customerRepository.findById(arg0).get();
        var addressDtoList = customer.getAddresses().stream().map(a ->
                new AddressDto(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode())).toList();
        return new CustomerDto(customer.getId(), customer.getName(), customer.getAge(),
                customer.getEmail(), addressDtoList);
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public CustomerDto getCustomerByEmail(@NotNull(message = "Customer email cannot be null.")  String arg0) {
        logger.info("getCustomerByEmail parameter: " + arg0);
        var customer = customerRepository.findByEmail(arg0).get();
        var addressDtoList = customer.getAddresses().stream().map(a ->
                new AddressDto(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode())).toList();
        return new CustomerDto(customer.getId(), customer.getName(), customer.getAge(),
                customer.getEmail(), addressDtoList);
    }

    @Transactional
    @Tool("""
            Creates a new customer with a list of addresses and returns the 
            created customer.
            """)
    public Customer createCustomerWithAddress(@NotNull @P("The customer object") Customer arg0,
                                              @NotNull @P("A list of address objects") List<Address> arg1) {
        logger.info("createCustomerWithAddress");
        arg0.setAddresses(arg1);
        arg1.forEach(address -> address.setCustomer(arg0));

        return customerRepository.save(arg0);
    }

    @Transactional
    @Tool("""
            Creates a new customer without addresses and returns the created customer.
            """)
    public Customer createCustomer(@NotNull @P("The customer object")  Customer arg0) {
        logger.info("createCustomer parameter: " + arg0);
        return customerRepository.save(arg0);
    }

}
