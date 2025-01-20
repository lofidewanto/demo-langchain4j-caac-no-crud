package com.github.caac.demo;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerCrudServiceIT {

    private final String email = "john.doe@example.com";
    @Autowired
    private CustomerCrudService customerCrudService;
    @Autowired
    private CustomerRepository customerRepository;

    private void createTestCustomer() {
        Customer customer = new Customer();
        customer.setName("Brother John");
        customer.setAge(40);
        customer.setEmail(email);

        customerRepository.save(customer);
    }

    private void deleteTestCustomer() {
        customerRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    void create_customer_with_email() {
        Customer customer = new Customer();
        customer.setName("Barry Doe");
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

    @Test
    @Transactional
    void get_customer_by_email() {
        createTestCustomer();

        Customer customerByEmail = customerCrudService.getCustomerByEmail(email);

        assertEquals(email, customerByEmail.getEmail());

        deleteTestCustomer();
    }
}
