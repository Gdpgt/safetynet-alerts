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
}
