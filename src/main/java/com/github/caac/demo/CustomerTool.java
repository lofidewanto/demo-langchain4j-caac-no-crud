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
public class CustomerTool {

    private static final Logger logger = LoggerFactory.getLogger(CustomerTool.class);

    private final CustomerRepository customerRepository;

    public CustomerTool(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves all customers  and returns a list of customers.
            """)
    public List<CustomerDto> getAllCustomers() {
        logger.info("getAllCustomers with no parameter");

        List<Customer> all = customerRepository.findAll();

        List<CustomerDto> customerDtos = all.stream().map(customer -> {
            var addressDtoList = customer.getAddresses().stream().map(a ->
                    new AddressDto(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode())).toList();
            return new CustomerDto(customer.getId(), customer.getName(), customer.getAge(),
                    customer.getEmail(), addressDtoList);
        }).toList();

        return customerDtos;
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the customer id.
            """)
    public CustomerDto getCustomerById(@NotNull(message = "Customer ID cannot be null.") Long customerId) {
        logger.info("getCustomerById parameter: " + customerId);

        var customer = customerRepository.findById(customerId).get();

        var addressDtoList = customer.getAddresses().stream().map(a ->
                new AddressDto(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode())).toList();
        return new CustomerDto(customer.getId(), customer.getName(), customer.getAge(),
                customer.getEmail(), addressDtoList);
    }

    @Transactional(readOnly = true)
    @Tool("""
            Retrieves a customer by the email.
            """)
    public CustomerDto getCustomerByEmail(@NotNull(message = "Customer email cannot be null.") String customerEmail) {
        logger.info("getCustomerByEmail parameter: " + customerEmail);

        var customer = customerRepository.findByEmail(customerEmail).get();

        var addressDtoList = customer.getAddresses().stream().map(a ->
                new AddressDto(a.getId(), a.getStreet(), a.getCity(), a.getState(), a.getZipCode())).toList();
        return new CustomerDto(customer.getId(), customer.getName(), customer.getAge(),
                customer.getEmail(), addressDtoList);
    }

    @Transactional
    @Tool("""
            Creates a new customer and returns the 
            created customer.
            """)
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerDto.name() == null || customerDto.email() == null || customerDto.age() == 0) {
            throw new IllegalArgumentException("Customer name, email and age cannot be null.");
        }

        logger.info("createCustomer with parameter: " + customerDto.name() + ", " + customerDto.email() + ", " + customerDto.age());

        Customer customer = new Customer(customerDto.email(), customerDto.name(), customerDto.age());
        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerDto(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getAge(),
                savedCustomer.getEmail(), null);
    }

}
