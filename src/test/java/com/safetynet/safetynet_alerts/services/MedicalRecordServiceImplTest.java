package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynet_alerts.services.impl.MedicalRecordServiceImpl;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;


    @Test
    void retrieveAll_shouldReturnAllMedicalRecords() {
        // Arrange
        List<MedicalRecord> records = List.of(TestUtils.createFakeMedicalRecord("John"));
        when(medicalRecordRepository.findAll()).thenReturn(records);

        // Act
        ResponseEntity<List<MedicalRecord>> response = medicalRecordService.retrieveAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }


    @Test
    void registerIfAbsent_shouldAddRecord_whenNotExists() {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();
        when(medicalRecordRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = medicalRecordService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Le dossier médical a été ajouté avec succès.", response.getBody());
    }


    @Test
    void registerIfAbsent_shouldReturnConflict_whenRecordAlreadyExists() {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();
        when(medicalRecordRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(dto.toMedicalRecord()));

        // Act
        ResponseEntity<String> response = medicalRecordService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Le dossier médical existe déjà.", response.getBody());
    }


    @Test
    void update_shouldModifyRecord_whenExists() {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();
        MedicalRecord existing = dto.toMedicalRecord();
        when(medicalRecordRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = medicalRecordService.update(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Le dossier médical a été mis à jour.", response.getBody());
    }


    @Test
    void update_shouldReturnNotFound_whenRecordDoesNotExist() {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();
        when(medicalRecordRepository.findOptionalByFirstAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = medicalRecordService.update(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Le dossier médical n'existe pas.", response.getBody());
    }


    @Test
    void delete_shouldRemoveRecord_whenExists() {
        // Arrange
        MedicalRecord existing = TestUtils.createFakeMedicalRecord("John");
        when(medicalRecordRepository.findOptionalByFirstAndLastName("John", "Doe"))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = medicalRecordService.delete("John", "Doe");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Le dossier médical a été supprimé avec succès.", response.getBody());
    }


    @Test
    void delete_shouldReturnNotFound_whenRecordDoesNotExist() {
        // Arrange
        when(medicalRecordRepository.findOptionalByFirstAndLastName("John", "Doe"))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = medicalRecordService.delete("John", "Doe");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Le dossier médical n'existe pas.", response.getBody());
    }


    @Test
    void getBirthdatesOfPersons_shouldReturnBirthdatesOfCoveredPersons() {
        // Arrange
        Person p1 = TestUtils.createFakePerson("Alice", "Ebob");
        Person p2 = TestUtils.createFakePerson("John", "Doe");
        LocalDate date1 = LocalDate.of(1993, 1, 1);
        LocalDate date2 = LocalDate.of(2005, 1, 1);
        MedicalRecord m1 = TestUtils.createFakeMedicalRecord("Alice", "Ebob", date1);
        MedicalRecord m2 = TestUtils.createFakeMedicalRecord("John", "Doe", date2);
        when(medicalRecordRepository.findAll()).thenReturn(List.of(m1, m2));

        // Act
        List<LocalDate> birthdates = medicalRecordService.getBirthdatesOfPersons(Set.of(p1, p2));

        // Assert
        assertEquals(List.of(date1, date2), birthdates);
    }


    @Test
    void getMedicalRecordByFirstAndLastName_shouldReturnRecord_whenExists() {
        // Arrange
        MedicalRecord record = TestUtils.createFakeMedicalRecord("John", "Doe", LocalDate.of(1993, 1, 1));
        when(medicalRecordRepository.findAll()).thenReturn(List.of(record));

        // Act
        MedicalRecord result = medicalRecordService.getMedicalRecordByFirstAndLastName("John", "Doe");

        // Assert
        assertEquals(record, result);
    }


    @Test
    void getMedicalRecordByFirstAndLastName_shouldThrowNotFound_whenAbsent() {
        // Arrange
        when(medicalRecordRepository.findAll()).thenReturn(List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> medicalRecordService.getMedicalRecordByFirstAndLastName("John", "Doe"));
    }

}
