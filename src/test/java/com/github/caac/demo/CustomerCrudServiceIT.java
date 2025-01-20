package com.github.caac.demo;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    void create_customer_with_one_address() {
        Customer customer = new Customer();
        customer.setName("Barry Doe");
        customer.setAge(30);
        customer.setEmail("john.doe@example.com");

        Address address = new Address();
        address.setStreet("1234 Main Street");
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");

        Customer cutomerCreated = customerCrudService.createCustomerWithAddress(customer, List.of(address));

        assertNotNull(cutomerCreated);
        assertEquals("john.doe@example.com", cutomerCreated.getEmail());
        assertTrue(customerRepository.findById(cutomerCreated.getId()).isPresent());
        assertEquals("10001", cutomerCreated.getAddresses().get(0).getZipCode());
    }

    @Test
    @Transactional
    void create_customer() {
        Customer customer = new Customer();
        customer.setName("Barry Doe");
        customer.setAge(30);
        customer.setEmail("john.doe@example.com");

        Customer cutomerCreated = customerCrudService.createCustomer(customer);

        assertNotNull(cutomerCreated);
        assertEquals("john.doe@example.com", cutomerCreated.getEmail());
        assertTrue(customerRepository.findById(cutomerCreated.getId()).isPresent());
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
