package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

    @Mock
    JsonDataLoader jsonDataLoader;

    @InjectMocks
    PersonRepository personRepository;

    @Test
    void findAll_shouldReturnAllPersons() {
        // Arrange
        List<Person> persons = List.of(
                TestUtils.createFakePerson("Alice")
        );
        when(jsonDataLoader.getPersons()).thenReturn(persons);

        // Act
        List<Person> result = personRepository.findAll();

        // Assert
        assertEquals(persons, result);
    }


    @Test
    void findOptionalByFirstAndLastName_shouldReturnPerson_whenExists() {
        // Arrange
        Person alice = TestUtils.createFakePerson("Alice", "Ebob");
        when(jsonDataLoader.getPersons()).thenReturn(
                List.of(
                        alice
                )
        );

        // Act
        Optional<Person> result = personRepository.findOptionalByFirstAndLastName("Alice", "Ebob");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(alice, result.get());
    }


    @Test
    void add_shouldAppendPerson() {
        // Arrange
        List<Person> persons = new ArrayList<>();
        when(jsonDataLoader.getPersons()).thenReturn(persons);
        Person newPerson = TestUtils.createFakePerson("John");

        // Act
        personRepository.add(newPerson);

        // Assert
        assertEquals(1, persons.size());
        assertTrue(persons.contains(newPerson));
    }


    @Test
    void updatePersonalInformation_shouldModifyFields() {
        // Arrange
        Person existing = TestUtils.createFakePerson("Alice", "Ebob",
                "Old", "OldCity", "0600000000", "old@mail.com");
        Person updated = TestUtils.createFakePerson("Alice", "Ebob",
                "New", "NewCity", "0700000000", "new@mail.com");

        // Act
        personRepository.updatePersonalInformation(existing, updated);

        // Assert
        assertEquals("New", existing.getAddress());
        assertEquals("NewCity", existing.getCity());
        assertEquals("0700000000", existing.getPhone());
        assertEquals("new@mail.com", existing.getEmail());
    }


    @Test
    void delete_shouldRemovePerson() {
        // Arrange
        Person alice = TestUtils.createFakePerson("Alice");
        List<Person> persons = new ArrayList<>(List.of(alice));
        when(jsonDataLoader.getPersons()).thenReturn(persons);

        // Act
        personRepository.delete(alice);

        // Assert
        assertTrue(persons.isEmpty());
    }


    @Test
    void findAllByAddresses_shouldReturnMatchingPersons() {
        // Arrange
        Person p1 = TestUtils.createFakePerson("Alice", "Ebob", "10 Downing Street", "City", "0600000000", "alice@ebob.com");
        Person p2 = TestUtils.createFakePerson("John", "Doe",  "36 quai des Orfèvres", "City", "0700000000", "john@doe.com");
        when(jsonDataLoader.getPersons()).thenReturn(List.of(p1, p2));
        Set<String> addresses = Set.of("10 Downing Street");

        // Act
        Set<Person> result = personRepository.findAllByAddresses(addresses);

        // Assert
        assertEquals(Set.of(p1), result);
    }


    @Test
    void findAllByAddress_shouldReturnList_whenAddressMatches() {
        // Arrange
        Person p1 = TestUtils.createFakePerson("Alice", "Ebob", "10 Downing Street", "City", "060000000", "alice@ebob.com");
        when(jsonDataLoader.getPersons()).thenReturn(List.of(p1));

        // Act
        List<Person> result = personRepository.findAllByAddress("10 Downing Street");

        // Assert
        assertEquals(List.of(p1), result);
    }


    @Test
    void findAllByLastName_shouldReturnList_whenLastNameMatches() {
        // Arrange
        Person p1 = TestUtils.createFakePerson("Alice", "Ebob", "10 Downing Street", "City", "060000000", "alice@ebob.com");
        Person p2 = TestUtils.createFakePerson("John", "Ebob",  "36 quai des Orfèvres", "City", "0700000000", "john@ebob.com");
        when(jsonDataLoader.getPersons()).thenReturn(List.of(p1, p2));

        // Act
        List<Person> result = personRepository.findAllByLastName("Ebob");

        // Assert
        assertEquals(List.of(p1, p2), result);
    }


    @Test
    void findAllByCity_shouldReturnList_whenCityMatches() {
        // Arrange
        Person p1 = TestUtils.createFakePerson("Alice", "Ebob", "10 Downing Street", "Paris", "060000000", "alice@ebob.com");
        when(jsonDataLoader.getPersons()).thenReturn(List.of(p1));

        // Act
        List<Person> result = personRepository.findAllByCity("Paris");

        // Assert
        assertEquals(List.of(p1), result);
    }

}
