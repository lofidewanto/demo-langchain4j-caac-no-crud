package com.github.caac.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final AddressRepository addressRepository;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository,
                           AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        return customer.orElse(null);
    }

    @Transactional
    @Validated
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    @Validated
    public Customer createCustomerWithAddress(Customer customer, List<Address> addresses) {
        customer.setAddresses(addresses);
        addresses.forEach(address -> address.setCustomer(customer));

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteAllCustomers() {
        addressRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }
}
