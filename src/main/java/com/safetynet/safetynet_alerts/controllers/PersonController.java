package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return personService.getAllPersons();
    }


    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody PersonDTO personDTO) {
        return personService.addPersonWithoutDuplicate(personDTO.toPerson());
    }


    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody PersonDTO personDTO) {
        return personService.updatePerson(personDTO.toPerson());
    }


    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        return personService.deletePerson(firstName, lastName);
    }
}
