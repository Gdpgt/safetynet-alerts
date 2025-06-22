package com.safetynet.safetynet_alerts.utils;

import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.Person;

public class TestUtils {

    // Pour les persons
    public static Person createFakePerson(String firstName) {
        return Person.builder().firstName(firstName).build();
    }


    public static PersonDTO createFakePersonDTO() {
        return PersonDTO.builder().firstName("Alice").build();
    }


    // Pour les firestations
    public static Firestation createFakeFirestation(String address, int stationNumber) {
        return new Firestation(address, stationNumber);
    }


    public static FirestationDTO createFakeFirestationDTO() {
        return FirestationDTO.builder().address("10 Downing Street").station(1).build();
    }


    public static UpdateFirestationDTO createFakeUpdateFirestationDTO() {
        return UpdateFirestationDTO.builder().address("10 Downing Street").oldStation(1).newStation(2).build();
    }

}
