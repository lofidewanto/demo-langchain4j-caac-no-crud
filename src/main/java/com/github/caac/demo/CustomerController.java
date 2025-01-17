package com.github.caac.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    
    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    private final CustomerCrudService customerCrudService;

    private final AddressCrudService addressCrudService;

    private final CustomerAgentService customerAgentService;
    
    public CustomerController(CustomerCrudService customerCrudService, AddressCrudService addressCrudService, CustomerAgentService customerAgentService) {
        this.customerCrudService = customerCrudService;
        this.addressCrudService = addressCrudService;
        this.customerAgentService = customerAgentService;
    }
    
    @GetMapping("/customer_crud/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        logger.debug("Getting customer with id: {}", id);
        return customerCrudService.getCustomer(id);
    }

    @PostMapping("/customer_crud")
    public Customer createCustomerWithAddress(@RequestBody Customer customer, @RequestParam List<Address> addresses) {
        logger.debug("Creating customer with addresses: {}", customer);
        return customerCrudService.createCustomerWithAddress(customer, addresses);
    }

}
