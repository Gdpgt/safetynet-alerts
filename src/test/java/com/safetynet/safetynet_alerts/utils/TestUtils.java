package com.safetynet.safetynet_alerts.utils;

import java.util.List;
import java.util.Set;
import java.time.LocalDate;

import com.safetynet.safetynet_alerts.dto.AddressAndItsResidentsDTO;
import com.safetynet.safetynet_alerts.dto.ChildDTO;
import com.safetynet.safetynet_alerts.dto.ChildrenAndFamilyMembersByAddressDTO;
import com.safetynet.safetynet_alerts.dto.FamilyMemberDTO;
import com.safetynet.safetynet_alerts.dto.FirestationCoverageDTO;
import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.dto.PersonCoveredByFirestationDTO;
import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.dto.PhoneNumbersByFirestationNumberDTO;
import com.safetynet.safetynet_alerts.dto.ResidentByAddressDTO;
import com.safetynet.safetynet_alerts.dto.ResidentByLastNameDTO;
import com.safetynet.safetynet_alerts.dto.ResidentsAndStationByAddressDTO;
import com.safetynet.safetynet_alerts.dto.ResidentsByFirestationNumbersDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;

public class TestUtils {

    // Pour les persons
    public static Person createFakePerson(String firstName) {
        return Person.builder()
                .firstName(firstName)
                .build();
    }


    public static Person createFakePerson(String firstName, String lastName) {
        return Person.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }


    public static Person createFakePerson(String first, String last,
                                          String address, String city,
                                          String phone, String email) {
        return Person.builder()
                .firstName(first)
                .lastName(last)
                .address(address)
                .city(city)
                .phone(phone)
                .email(email)
                .build();
    }


    public static PersonDTO createFakePersonDTO() {
        return PersonDTO.builder()
                .firstName("Alice")
                .build();
    }


    // Pour les firestations
    public static Firestation createFakeFirestation(String address, int stationNumber) {
        return Firestation.builder()
                .address(address)
                .station(stationNumber)
                .build();
    }


    public static FirestationDTO createFakeFirestationDTO() {
        return FirestationDTO.builder()
                .address("10 Downing Street")
                .station(1)
                .build();
    }


    public static UpdateFirestationDTO createFakeUpdateFirestationDTO() {
        return UpdateFirestationDTO.builder()
                .address("10 Downing Street")
                .oldStation(1)
                .newStation(2)
                .build();
    }


    // Pour les medicalRecords
    public static MedicalRecord createFakeMedicalRecord(String firstName) {
        return MedicalRecord.builder()
                .firstName(firstName)
                .build();
    }


    public static MedicalRecord createFakeMedicalRecord(String first, String last, LocalDate birthdate) {
        return MedicalRecord.builder()
                .firstName(first)
                .lastName(last)
                .birthdate(birthdate)
                .medications(List.of())
                .allergies(List.of())
                .build();
    }


    public static MedicalRecordDTO createFakeMedicalRecordDTO() {
        return MedicalRecordDTO.builder()
                .firstName("Alice")
                .build();
    }


    // Pour les alertes
    public static FirestationCoverageDTO createFakeFirestationCoverageDTO() {
        return new FirestationCoverageDTO(
                List.of(PersonCoveredByFirestationDTO.builder()
                        .firstName("John")
                        .build()), 1, 0
        );
    }

    public static ChildrenAndFamilyMembersByAddressDTO createFakeChildrenAndFamilyMembersByAddressDTO() {
        return new ChildrenAndFamilyMembersByAddressDTO(
                List.of(new ChildDTO("Enfant", "Doe", 5)),
                List.of(new FamilyMemberDTO("Parent", "Doe", 77))
        );
    }

    public static PhoneNumbersByFirestationNumberDTO createFakePhoneNumbersByFirestationNumberDTO() {
        return new PhoneNumbersByFirestationNumberDTO(Set.of("0600000000"));
    }

    public static ResidentsAndStationByAddressDTO createFakeResidentsAndStationByAddressDTO() {
        return new ResidentsAndStationByAddressDTO(
                List.of(ResidentByAddressDTO.builder()
                        .lastName("Doe")
                        .build()), 1
        );
    }

    public static ResidentsByFirestationNumbersDTO createFakeResidentsByFirestationNumbersDTO() {
        return new ResidentsByFirestationNumbersDTO(
                List.of(new AddressAndItsResidentsDTO("10 Downing Street", List.of()))
        );
    }

    public static ResidentByLastNameDTO createFakeResidentByLastNameDTO() {
        return ResidentByLastNameDTO.builder()
                .lastName("Doe")
                .build();
    }

}
