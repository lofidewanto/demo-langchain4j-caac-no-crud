package com.github.caac.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
public class PersonCrudService {
    
    private PersonRepository personRepository;

    public PersonCrudService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    @Transactional(readOnly = true)
    public Person getPerson(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    @Validated
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    @Validated
    public Person createPersonWithAddress(Person person, List<Address> addresses) {
        // Create a person only with some addresses
        person.setAddresses(addresses);
        addresses.forEach(address -> address.setPerson(person));

        return personRepository.save(person);
    }
}
