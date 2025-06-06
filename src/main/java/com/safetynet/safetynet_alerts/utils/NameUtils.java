package com.safetynet.safetynet_alerts.utils;

import com.safetynet.safetynet_alerts.models.Person;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NameUtils {

    public static Set<String> getFullNamesOfPersons(List<Person> persons) {
        return persons.stream().map(p -> p.getFirstName() + " " + p.getLastName()).collect(Collectors.toSet());
    }

}
