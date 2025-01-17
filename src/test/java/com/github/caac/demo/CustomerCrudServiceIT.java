package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class CustomerCrudServiceIT {

    @Autowired
    private CustomerCrudService customerCrudService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    void create_customer_with_email() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setAge(30);
        customer.setEmail("john.doe@example.com");

        Customer result = customerCrudService.createCustomer(customer);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        assertTrue(customerRepository.findById(result.getId()).isPresent());
    }

    @Test
    @Transactional
    void create_customer_with_invalid_email() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer.setAge(25);
        customer.setEmail("invalid-email");

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            customerCrudService.createCustomer(customer);
        });

        System.out.println(exception.getMessage());
    }
}
