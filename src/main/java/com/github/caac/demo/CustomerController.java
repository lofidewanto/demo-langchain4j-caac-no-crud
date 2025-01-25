package com.github.caac.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    private final AddressService addressService;

    private final CustomerAgent customerAgent;

    public CustomerController(CustomerService customerService, AddressService addressService, CustomerAgent customerAgent) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.customerAgent = customerAgent;
    }

    @GetMapping("/customer_crud/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        logger.debug("Getting customer with id: {}", id);
        return customerService.getCustomerById(id);
    }

    @PostMapping("/customer_crud")
    public Customer createCustomerWithAddress(@RequestBody Customer customer, @RequestParam List<Address> addresses) {
        logger.debug("Creating customer with addresses: {}", customer);
        return customerService.createCustomerWithAddress(customer, addresses);
    }

}
