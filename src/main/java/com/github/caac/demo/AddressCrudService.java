package com.github.caac.demo;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressCrudService {
    
    private AddressRepository addressRepository;

    public AddressCrudService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    @Transactional(readOnly = true)
    public Address getAddress(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.orElse(null);
    }

    @Transactional
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }
}
