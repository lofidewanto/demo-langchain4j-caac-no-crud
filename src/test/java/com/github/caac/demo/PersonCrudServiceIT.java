package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class PersonCrudServiceIT {

    @Autowired
    private PersonCrudService personCrudService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Transactional
    public void create_person_with_email() {
        Person person = new Person();
        person.setName("John Doe");
        person.setAge(30);
        person.setEmail("john.doe@example.com");

        Person result = personCrudService.createPerson(person);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        assertTrue(personRepository.findById(result.getId()).isPresent());
    }

    @Test
    @Transactional
    public void create_person_with_invalid_email() {
        Person person = new Person();
        person.setName("Jane Doe");
        person.setAge(25);
        person.setEmail("invalid-email");

        assertThrows(ConstraintViolationException.class, () -> {
            personCrudService.createPerson(person);
        });
    }
}
