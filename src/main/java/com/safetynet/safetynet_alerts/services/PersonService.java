package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public ResponseEntity<List<Person>> retrieveAll() {
        List<Person> persons = personRepository.findAll();
        return ResponseEntity.ok(persons);
    }


    public ResponseEntity<String> registerIfAbsent(PersonDTO dto) {
        Optional<Person> existingPerson = personRepository.findByFirstAndLastName(dto.getFirstName(), dto.getLastName());

        if (existingPerson.isEmpty()) {
            personRepository.add(dto.toPerson());
            log.info("La personne \"{} {}\" vient d'être ajoutée.", dto.getFirstName(), dto.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body("La personne a été ajoutée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La personne existe déjà.");
        }
    }


    public ResponseEntity<String> updatePersonalInformation(PersonDTO dto) {
        Optional<Person> existingPerson = personRepository.findByFirstAndLastName(dto.getFirstName(), dto.getLastName());

        if (existingPerson.isPresent()) {
            personRepository.updatePersonalInformation(existingPerson.get(), dto.toPerson());
            log.info("Les données de la personne \"{} {}\" viennent d'être mis à jour.", dto.getFirstName(), dto.getLastName());
            return ResponseEntity.ok("La personne a été mise à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La personne n'existe pas.");
        }
    }


    public ResponseEntity<String> delete(String firstName, String lastName) {
        Optional<Person> existingPerson = personRepository.findByFirstAndLastName(firstName, lastName);

        if (existingPerson.isPresent()) {
            personRepository.delete(existingPerson.get());
            log.info("La personne \"{} {}\" vient d'être supprimée.", firstName, lastName);
            return ResponseEntity.ok("La personne a été supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La personne n'existe pas.");
        }
    }

}
