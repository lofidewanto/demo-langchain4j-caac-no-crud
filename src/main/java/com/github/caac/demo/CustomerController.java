package com.github.caac.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    private final AddressService addressService;

    private final CustomerAgent customerAgent;

    private final CustomerAgenticService customerAgenticService;

    public CustomerController(CustomerService customerService, AddressService addressService,
            CustomerAgent customerAgent, CustomerAgenticService customerAgenticService) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.customerAgent = customerAgent;
        this.customerAgenticService = customerAgenticService;
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

    @PostMapping("/customer_chat")
    public ResponseEntity<String> chat(String chatId, String userMessage) {
        String responseMessage = customerAgenticService.chatWithAgentsWithoutProtection(chatId, userMessage);

        return ResponseEntity.ok()
                .body(responseMessage);
    }

}
