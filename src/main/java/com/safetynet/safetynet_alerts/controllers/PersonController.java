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
    public ResponseEntity<List<Person>> getAll() {
        return personService.retrieveAll();
    }


    @PostMapping
    public ResponseEntity<String> add(@RequestBody PersonDTO personDTO) {
        return personService.registerIfAbsent(personDTO);
    }


    @PutMapping
    public ResponseEntity<String> update(@RequestBody PersonDTO personDTO) {
        return personService.updatePersonalInformation(personDTO);
    }


    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String firstName, @RequestParam String lastName) {
        return personService.delete(firstName, lastName);
    }
}
