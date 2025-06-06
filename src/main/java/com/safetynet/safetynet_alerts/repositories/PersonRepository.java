package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {

    private final JsonDataLoader jsonDataLoader;

    public PersonRepository (JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }


    public List<Person> findAll() {
        return jsonDataLoader.getPersons();
    }


    public Optional<Person> findOptionalByFirstAndLastName(String firstName, String lastName) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }


    public void add(Person person) {
        jsonDataLoader.getPersons().add(person);
    }


    public void updatePersonalInformation(Person existingPerson, Person updatedData) {
        existingPerson.setAddress(updatedData.getAddress());
        existingPerson.setCity(updatedData.getCity());
        existingPerson.setZip(updatedData.getZip());
        existingPerson.setPhone(updatedData.getPhone());
        existingPerson.setEmail(updatedData.getEmail());
    }


    public void delete(Person existingPerson) {
        jsonDataLoader.getPersons().remove(existingPerson);
    }


    public Set<Person> findAllByAddresses(Set<String> addresses) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .collect(Collectors.toSet());
    }


    public List<Person> findAllByAddress(String address) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();
    }


    public List<Person> findAllByLastName(String lastName) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .toList();
    }


    public List<Person> findAllByCity(String city) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .toList();
    }

}
