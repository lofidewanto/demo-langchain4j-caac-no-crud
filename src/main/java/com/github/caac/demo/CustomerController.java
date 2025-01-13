package com.github.caac.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    
    private static Logger log = LoggerFactory.getLogger(CustomerController.class);
    
    private final CustomerCrudService customerCrudService;

    private final AddressCrudService addressCrudService;

    private final CustomerAgentService customerAgentService;
    
    public CustomerController(CustomerCrudService customerCrudService, AddressCrudService addressCrudService, CustomerAgentService customerAgentService) {
        this.customerCrudService = customerCrudService;
        this.addressCrudService = addressCrudService;
        this.customerAgentService = customerAgentService;
    }
    
    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        log.debug("Getting customer with id: {}", id);
        return customerCrudService.getCustomer(id);
    }

}
