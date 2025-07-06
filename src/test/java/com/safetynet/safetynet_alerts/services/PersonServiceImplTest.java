package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import com.safetynet.safetynet_alerts.services.impl.PersonServiceImpl;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;


    @Test
    void retrieveAll_shouldReturnAllPersons() {
        // Arrange
        List<Person> persons = List.of(TestUtils.createFakePerson("Alice"));
        when(personRepository.findAll()).thenReturn(persons);

        // Act
        ResponseEntity<List<Person>> response = personService.retrieveAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(persons, response.getBody());
    }


    @Test
    void registerIfAbsent_shouldAddPerson_whenPersonDoesNotExist() {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();

        // Act
        ResponseEntity<String> response = personService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("La personne a été ajoutée avec succès.", response.getBody());
    }


    @Test
    void registerIfAbsent_shouldReturnConflict_whenPersonAlreadyExists() {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();
        Person existing = dto.toPerson();
        when(personRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = personService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("La personne existe déjà.", response.getBody());
    }


    @Test
    void updatePersonalInformation_shouldUpdatePerson_whenPersonExists() {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();
        Person existing = dto.toPerson();
        when(personRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = personService.updatePersonalInformation(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La personne a été mise à jour avec succès.", response.getBody());
    }


    @Test
    void updatePersonalInformation_shouldReturnNotFound_whenPersonDoesNotExist() {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();
        when(personRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = personService.updatePersonalInformation(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("La personne n'existe pas.", response.getBody());
    }


    @Test
    void delete_shouldRemovePerson_whenPersonExists() {
        // Arrange
        String firstName = "John";
        String lastName = "Boyd";
        Person person = TestUtils.createFakePerson(firstName);
        when(personRepository.findOptionalByFirstAndLastName(firstName, lastName))
                .thenReturn(Optional.of(person));

        // Act
        ResponseEntity<String> response = personService.delete(firstName, lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La personne a été supprimée avec succès.", response.getBody());
    }


    @Test
    void delete_shouldReturnNotFound_whenPersonDoesNotExist() {
        // Arrange
        when(personRepository.findOptionalByFirstAndLastName("John", "Doe"))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = personService.delete("John", "Doe");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("La personne n'existe pas.", response.getBody());
    }
}
