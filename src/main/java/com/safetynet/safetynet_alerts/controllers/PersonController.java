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

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Person>> getAll() {
        return personService.retrieveAll();
    }


    @PostMapping
    public ResponseEntity<String> add(@RequestBody PersonDTO dto) {
        return personService.registerIfAbsent(dto);
    }


    @PutMapping
    public ResponseEntity<String> update(@RequestBody PersonDTO dto) {
        return personService.updatePersonalInformation(dto);
    }


    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String firstName, @RequestParam String lastName) {
        return personService.delete(firstName, lastName);
    }
}
