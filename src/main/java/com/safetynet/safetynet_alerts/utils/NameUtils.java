package com.safetynet.safetynet_alerts.utils;

import com.safetynet.safetynet_alerts.models.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NameUtils {

    private static final Logger log = LoggerFactory.getLogger(NameUtils.class);

    public static Set<String> getFullNamesOfPersons(List<Person> persons) {
        Set<String> fullNames = persons.stream().map(p -> p.getFirstName() + " " + p.getLastName()).collect(Collectors.toSet());
        log.debug("Les prénoms et noms liés à cette liste de personnes : {} sont : {}.", persons, fullNames);
        return fullNames;
    }

}
