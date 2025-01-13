package com.github.caac.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
    
    private static Logger log = LoggerFactory.getLogger(PersonController.class);
    
    private final PersonCrudService personCrudService;
    private final AddressCrudService addressCrudService;
    
    public PersonController(PersonCrudService personCrudService, AddressCrudService addressCrudService) {
        this.personCrudService = personCrudService;
        this.addressCrudService = addressCrudService;
    }
    
    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable Long id) {
        log.debug("Getting person with id: {}", id);
        return personCrudService.getPerson(id);
    }

}
