package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonService {

    ResponseEntity<List<Person>> retrieveAll();

    ResponseEntity<String> registerIfAbsent(PersonDTO dto);

    ResponseEntity<String> updatePersonalInformation(PersonDTO dto);

    ResponseEntity<String> delete(String firstName, String lastName);
}
