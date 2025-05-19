package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PersonRepository {

    private final JsonDataLoader jsonDataLoader;

    public PersonRepository (JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }


    public List<Person> findAll() {
        return jsonDataLoader.getPersons();
    }


    public Optional<Person> findByFirstAndLastName(String firstName, String lastName) {
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


    public List<Person> findByAddresses(Set<String> addresses) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();
    }


    public List<Person> findByAddress(String address) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();
    }

}
