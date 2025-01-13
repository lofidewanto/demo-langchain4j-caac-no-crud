package com.github.caac.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PersonCrudServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonCrudService personCrudService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_person_with_address() {
        Person person = new Person();
        Address address1 = new Address();
        Address address2 = new Address();
        List<Address> addresses = Arrays.asList(address1, address2);

        when(personRepository.save(person)).thenReturn(person);

        Person result = personCrudService.createPersonWithAddress(person, addresses);

        assertNotNull(result);
        assertEquals(2, result.getAddresses().size());
        assertEquals(person, address1.getPerson());
        assertEquals(person, address2.getPerson());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    public void get_person() {
        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);

        when(personRepository.findById(personId)).thenReturn(Optional.of(person));

        Person result = personCrudService.getPerson(personId);

        assertNotNull(result);
        assertEquals(personId, result.getId());
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    public void create_person_with_email() {
        Person person = new Person();
        person.setEmail("test@example.com");

        when(personRepository.save(person)).thenReturn(person);

        Person result = personCrudService.createPerson(person);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(personRepository, times(1)).save(person);
    }
}
