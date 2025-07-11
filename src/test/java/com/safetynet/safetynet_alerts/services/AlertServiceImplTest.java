package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.assemblers.ResidentAssembler;
import com.safetynet.safetynet_alerts.dto.*;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import com.safetynet.safetynet_alerts.services.impl.AlertServiceImpl;
import com.safetynet.safetynet_alerts.services.impl.FirestationServiceImpl;
import com.safetynet.safetynet_alerts.services.impl.MedicalRecordServiceImpl;
import com.safetynet.safetynet_alerts.utils.AgeUtils;
import com.safetynet.safetynet_alerts.utils.DateUtils;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationServiceImpl firestationService;

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private ResidentAssembler residentAssembler;

    @InjectMocks
    private AlertServiceImpl alertService;


    @Test
    void retrievePersonsCoveredByFirestationNumber_shouldReturnDTO_whenStationExists() {
        // Arrange
        int station = 1;
        Set<String> addresses = Set.of("10 Downing Street");
        when(firestationService.getAddressesByStationNumber(station)).thenReturn(addresses);

        Person john = TestUtils.createFakePerson("John", "Doe",
                "10 Downing Street", "London",
                "0600000000", "john@doe.com");
        Set<Person> coveredPersons = Set.of(john);
        when(personRepository.findAllByAddresses(addresses)).thenReturn(coveredPersons);

        List<LocalDate> birthdates = List.of(LocalDate.of(1990, 1, 1));
        when(medicalRecordService.getBirthdatesOfPersons(coveredPersons)).thenReturn(birthdates);

        try (MockedStatic<AgeUtils> ageMock = mockStatic(AgeUtils.class)) {
            ageMock.when(() -> AgeUtils.countNumberOfAdults(birthdates)).thenReturn(1L);
            ageMock.when(() -> AgeUtils.countNumberOfChildren(birthdates)).thenReturn(0L);

            // Act
            ResponseEntity<FirestationCoverageDTO> response =
                    alertService.retrievePersonsCoveredByFirestationNumber(station);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            FirestationCoverageDTO body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.getCoveredPersons().size());
            assertEquals(1, body.getNumberOfAdults());
            assertEquals(0, body.getNumberOfChildren());
        }
    }


    @Test
    void retrievePersonsCoveredByFirestationNumber_shouldThrowNotFound_whenStationUnknown() {
        // Arrange
        when(firestationService.getAddressesByStationNumber(99)).thenReturn(Set.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrievePersonsCoveredByFirestationNumber(99));
    }


    @Test
    void retrieveChildrenAndFamilyMembersByAddress_shouldReturnDTO_whenChildrenPresent() {
        // Arrange
        String address = "10 Downing Street";
        Person child = TestUtils.createFakePerson("Enfant", "Doe", address, "London", "111", "Enfant@mail.com");
        Person adult = TestUtils.createFakePerson("Parent", "Doe", address, "London", "222", "parent@mail.com");
        when(personRepository.findAllByAddress(address)).thenReturn(List.of(child, adult));

        MedicalRecord mrChild  = TestUtils.createFakeMedicalRecord("Enfant", "Doe",  LocalDate.of(2015, 1, 1));
        MedicalRecord mrAdult  = TestUtils.createFakeMedicalRecord("Parent", "Doe", LocalDate.of(1980, 1, 1));
        when(medicalRecordRepository.findAll()).thenReturn(List.of(mrChild, mrAdult));

        try (MockedStatic<DateUtils> dateMock = mockStatic(DateUtils.class)) {
            dateMock.when(() -> DateUtils.calculateAge(mrChild.getBirthdate())).thenReturn(10);
            dateMock.when(() -> DateUtils.calculateAge(mrAdult.getBirthdate())).thenReturn(45);

            // Act
            ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> response =
                    alertService.retrieveChildrenAndFamilyMembersByAddress(address);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ChildrenAndFamilyMembersByAddressDTO dto = response.getBody();
            assertNotNull(dto);
            assertEquals(1, dto.getChildren().size());
            assertEquals(1, dto.getFamilyMembers().size());
        }
    }


    @Test
    void retrieveChildrenAndFamilyMembersByAddress_shouldReturnNullBody_whenNoChildren() {
        // Arrange
        String address = "36 quai des Orfèvres";
        Person adult = TestUtils.createFakePerson("Alice", "Ebob", address, "Paris", "333", "alice@mail.com");
        when(personRepository.findAllByAddress(address)).thenReturn(List.of(adult));

        MedicalRecord mrAdult = TestUtils.createFakeMedicalRecord("Alice", "Ebob", LocalDate.of(1980, 1, 1));
        when(medicalRecordRepository.findAll()).thenReturn(List.of(mrAdult));

        try (MockedStatic<DateUtils> dateMock = mockStatic(DateUtils.class)) {
            dateMock.when(() -> DateUtils.calculateAge(mrAdult.getBirthdate())).thenReturn(40);

            // Act
            ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> response =
                    alertService.retrieveChildrenAndFamilyMembersByAddress(address);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
        }
    }


    @Test
    void retrieveChildrenAndFamilyMembersByAddress_shouldThrowNotFound_whenAddressUnknown() {
        // Arrange
        when(personRepository.findAllByAddress("Unknown")).thenReturn(List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrieveChildrenAndFamilyMembersByAddress("Unknown"));
    }


    @Test
    void retrievePhoneNumbersByFirestationNumber_shouldReturnDTO_whenResidentsFound() {
        // Arrange
        int station = 3;
        Set<String> addresses = Set.of("10 Downing Street");
        when(firestationService.getAddressesByStationNumber(station)).thenReturn(addresses);

        Person john = TestUtils.createFakePerson("John", "Doe", "10 Downing Street", "London", "0600000000", "john@doe.com");
        when(personRepository.findAllByAddresses(addresses)).thenReturn(Set.of(john));

        // Act
        ResponseEntity<PhoneNumbersByFirestationNumberDTO> response =
                alertService.retrievePhoneNumbersByFirestationNumber(station);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<String> phones = response.getBody().getPhoneNumbersByFirestationNumber();
        assertEquals(Set.of("0600000000"), phones);
    }


    @Test
    void retrievePhoneNumbersByFirestationNumber_shouldThrowNotFound_whenStationUnknown() {
        // Arrange
        when(firestationService.getAddressesByStationNumber(77)).thenReturn(Set.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrievePhoneNumbersByFirestationNumber(77));
    }


    @Test
    void retrievePhoneNumbersByFirestationNumber_shouldThrowNotFound_whenNoResidents() {
        // Arrange
        when(firestationService.getAddressesByStationNumber(4)).thenReturn(Set.of(""));
        when(personRepository.findAllByAddresses(Set.of(""))).thenReturn(Set.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrievePhoneNumbersByFirestationNumber(4));
    }


    @Test
    void retrieveResidentsAndStationByAddress_shouldReturnDTO_whenDataExists() {
        // Arrange
        String address = "10 Downing Street";
        when(firestationService.getStationNumberByAddress(address)).thenReturn(Optional.of(1));

        List<Person> persons = List.of(TestUtils.createFakePerson("John", "Doe", address,
                "London", "111", "john@doe.com"));
        when(personRepository.findAllByAddress(address)).thenReturn(persons);

        List<ResidentByAddressDTO> residents = List.of(ResidentByAddressDTO.builder().lastName("Doe").build());
        when(residentAssembler.createResidentsByAddressDTO(persons)).thenReturn(residents);

        // Act
        ResponseEntity<ResidentsAndStationByAddressDTO> response =
                alertService.retrieveResidentsAndStationByAddress(address);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResidentsAndStationByAddressDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getFirestationNumber());
        assertEquals(residents, body.getResidents());
    }


    @Test
    void retrieveResidentsAndStationByAddress_shouldThrowNotFound_whenNoStation() {
        // Arrange
        when(firestationService.getStationNumberByAddress("")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrieveResidentsAndStationByAddress(""));
    }


    @Test
    void retrieveResidentsByFirestationNumbers_shouldReturnDTO_whenAddressesFound() {
        // Arrange
        Set<Integer> stations = Set.of(1, 2);
        Set<String> addresses = Set.of("10 Downing Street", "36 quai des Orfèvres");
        when(firestationService.getAddressesByStationNumbers(stations)).thenReturn(addresses);

        AddressAndItsResidentsDTO dto1 = new AddressAndItsResidentsDTO("10 Downing Street", List.of());
        AddressAndItsResidentsDTO dto2 = new AddressAndItsResidentsDTO("36 quai des Orfèvres", List.of());
        when(residentAssembler.buildAddressResidentsDTO("10 Downing Street")).thenReturn(dto1);
        when(residentAssembler.buildAddressResidentsDTO("36 quai des Orfèvres")).thenReturn(dto2);

        // Act
        ResponseEntity<ResidentsByFirestationNumbersDTO> response =
                alertService.retrieveResidentsByFirestationNumbers(stations);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AddressAndItsResidentsDTO> body = response.getBody().getAddressesAndTheirResidents();
        assertEquals(2, body.size());
        assertTrue(body.contains(dto1));
        assertTrue(body.contains(dto2));
    }


    @Test
    void retrieveResidentsByFirestationNumbers_shouldThrowNotFound_whenNoAddresses() {
        // Arrange
        when(firestationService.getAddressesByStationNumbers(Set.of(42))).thenReturn(Set.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrieveResidentsByFirestationNumbers(Set.of(42)));
    }


    @Test
    void retrievePersonsInfoByLastName_shouldReturnDTO_whenPersonsExist() {
        // Arrange
        String lastName = "Ebob";
        List<Person> persons = List.of(TestUtils.createFakePerson("Alice", lastName,
                "123", "Paris", "555", "alice@ebob.com"));
        when(personRepository.findAllByLastName(lastName)).thenReturn(persons);

        List<ResidentByLastNameDTO> dto = List.of(ResidentByLastNameDTO.builder().lastName("Ebob").build());
        when(residentAssembler.createResidentsByLastNameDTO(persons)).thenReturn(dto);

        // Act
        ResponseEntity<List<ResidentByLastNameDTO>> response =
                alertService.retrievePersonsInfoByLastName(lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }


    @Test
    void retrievePersonsInfoByLastName_shouldThrowNotFound_whenNoneFound() {
        // Arrange
        when(personRepository.findAllByLastName("Unknown")).thenReturn(List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrievePersonsInfoByLastName("Unknown"));
    }


    @Test
    void retrieveEmailsByCity_shouldReturnEmails_whenPersonsFound() {
        // Arrange
        String city = "Paris";
        Person alice = TestUtils.createFakePerson("Alice", "Ebob", "123", city, "0600000000", "alice@ebob.com");
        when(personRepository.findAllByCity(city)).thenReturn(List.of(alice));

        // Act
        ResponseEntity<Set<String>> response = alertService.retrieveEmailsByCity(city);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Set.of("alice@ebob.com"), response.getBody());
    }


    @Test
    void retrieveEmailsByCity_shouldThrowNotFound_whenNoPersons() {
        // Arrange
        when(personRepository.findAllByCity("GhostTown")).thenReturn(List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> alertService.retrieveEmailsByCity("GhostTown"));
    }

}
