package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    private final JsonDataLoader jsonDataLoader;

    public PersonRepository (JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<Person> findAll() {
        return jsonDataLoader.getPersons();
    }

    public Person findByFirstAndLastName(String firstName, String lastName) {
        return jsonDataLoader.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    public void addPerson(Person person) {
        jsonDataLoader.getPersons().add(person);
    }

    public void updatePersonFields(Person existingPerson, Person updatedData) {
        existingPerson.setAddress(updatedData.getAddress());
        existingPerson.setCity(updatedData.getCity());
        existingPerson.setZip(updatedData.getZip());
        existingPerson.setPhone(updatedData.getPhone());
        existingPerson.setEmail(updatedData.getEmail());
    }

    public void deletePerson(Person person) {
        jsonDataLoader.getPersons().remove(person);
    }

}
