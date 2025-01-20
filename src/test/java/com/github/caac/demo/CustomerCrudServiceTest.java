package com.github.caac.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CustomerCrudServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerCrudService customerCrudService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_customer_with_address() {
        Customer customer = new Customer();
        Address address1 = new Address();
        Address address2 = new Address();
        List<Address> addresses = Arrays.asList(address1, address2);

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerCrudService.createCustomerWithAddress(customer, addresses);

        assertNotNull(result);
        assertEquals(2, result.getAddresses().size());
        assertEquals(customer, address1.getCustomer());
        assertEquals(customer, address2.getCustomer());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void get_customer() {
        Long personId = 1L;
        Customer customer = new Customer();
        customer.setId(personId);

        when(customerRepository.findById(personId)).thenReturn(Optional.of(customer));

        Customer result = customerCrudService.getCustomerById(personId);

        assertNotNull(result);
        assertEquals(personId, result.getId());
        verify(customerRepository, times(1)).findById(personId);
    }

    @Test
    void create_customer_with_email() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerCrudService.createCustomer(customer);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }
}
