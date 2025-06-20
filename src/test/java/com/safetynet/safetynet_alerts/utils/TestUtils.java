package com.safetynet.safetynet_alerts.utils;

import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;

public class TestUtils {


    public static Person createFakePerson(String firstName) {
        return Person.builder().firstName(firstName).build();
    }


    public static PersonDTO createFakePersonDTO() {
        return PersonDTO.builder().firstName("Alice").build();
    }

}
