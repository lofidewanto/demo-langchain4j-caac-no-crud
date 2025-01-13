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
    public Person createPerson(@Validated Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public Person createPersonWithAddress(Person person, List<Address> addresses) {
        person.setAddresses(addresses);
        addresses.forEach(address -> address.setPerson(person));

        return personRepository.save(person);
    }
}
