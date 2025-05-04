package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return ResponseEntity.ok(persons);
    }


    public ResponseEntity<String> addPersonWithoutDuplicate(Person person) {
        Person existingPerson = personRepository.findByFirstAndLastName(person.getFirstName(), person.getLastName());
        if (existingPerson == null) {
            personRepository.addPerson(person);
            log.info("La personne \"{} {}\" vient d'être ajoutée.", person.getFirstName(), person.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body("Personne ajoutée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La personne existe déjà.");
        }
    }


    public ResponseEntity<String> updatePerson(Person person) {
        Person existingPerson = personRepository.findByFirstAndLastName(person.getFirstName(), person.getLastName());
        if (existingPerson != null) {
            personRepository.updatePersonFields(existingPerson, person);
            log.info("Les données de la personne \"{} {}\" viennent d'être mis à jour.", person.getFirstName(), person.getLastName());
            return ResponseEntity.ok("Personne mise à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette personne n'existe pas.");
        }
    }


    public ResponseEntity<String> deletePerson(String firstName, String lastName) {
        Person existingPerson = personRepository.findByFirstAndLastName(firstName, lastName);
        if (existingPerson != null) {
            personRepository.deletePerson(existingPerson);
            log.info("La personne \"{} {}\" vient d'être supprimée.", firstName, lastName);
            return ResponseEntity.ok("Personne supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette personne n'existe pas.");
        }
    }

}
