package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

    @Mock
    JsonDataLoader jsonDataLoader;

    @InjectMocks
    MedicalRecordRepository medicalRecordRepository;

    
    @Test
    void findAll_shouldReturnAllMedicalRecords() {
        // Arrange
        List<MedicalRecord> medicalRecords = List.of(
                TestUtils.createFakeMedicalRecord("Alice", "Ebob", TestUtils.parseDate("01/01/1990")
                        , List.of("paracetamol:1g"), List.of("peanut"))
        );
        when(jsonDataLoader.getMedicalRecords()).thenReturn(medicalRecords);

        // Act
        List<MedicalRecord> result = medicalRecordRepository.findAll();

        // Assert
        assertEquals(medicalRecords, result);
    }

    
    @Test
    void findOptionalByFirstAndLastName_shouldReturnMedicalRecord_whenExists() {
        // Arrange
        MedicalRecord alice = TestUtils.createFakeMedicalRecord("Alice", "Ebob"
                , TestUtils.parseDate("01/01/1990"), List.of(), List.of());
        when(jsonDataLoader.getMedicalRecords()).thenReturn(List.of(alice));

        // Act
        Optional<MedicalRecord> result = medicalRecordRepository.findOptionalByFirstAndLastName("Alice", "Ebob");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(alice, result.get());
    }

    
    @Test
    void add_shouldAppendMedicalRecord() {
        // Arrange
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        when(jsonDataLoader.getMedicalRecords()).thenReturn(medicalRecords);
        MedicalRecord newRecord = TestUtils.createFakeMedicalRecord("John", "Doe", TestUtils.parseDate("01/01/1990")
                , List.of("ibuprofen:2g"), List.of("lactose"));

        // Act
        medicalRecordRepository.add(newRecord);

        // Assert
        assertEquals(1, medicalRecords.size());
        assertTrue(medicalRecords.contains(newRecord));
    }


    @Test
    void update_shouldModifyFields() {
        // Arrange
        MedicalRecord existing = TestUtils.createFakeMedicalRecord("Alice", "Ebob"
                , TestUtils.parseDate("01/01/1990")
                , List.of(), List.of());
        MedicalRecord updated = TestUtils.createFakeMedicalRecord("Alice", "Ebob"
                , TestUtils.parseDate("02/02/1992")
                , List.of("paracetamol:2g"), List.of("lactose"));

        // Act
        medicalRecordRepository.update(existing, updated);

        // Assert
        assertEquals(TestUtils.parseDate("02/02/1992"), existing.getBirthdate());
        assertEquals(List.of("paracetamol:2g"), existing.getMedications());
        assertEquals(List.of("lactose"), existing.getAllergies());
    }


    @Test
    void delete_shouldRemoveMedicalRecord() {
        // Arrange
        MedicalRecord alice = TestUtils.createFakeMedicalRecord("Alice", "Ebob", TestUtils.parseDate("01/01/1990")
                , List.of(), List.of());
        List<MedicalRecord> medicalRecords = new ArrayList<>(List.of(alice));
        when(jsonDataLoader.getMedicalRecords()).thenReturn(medicalRecords);

        // Act
        medicalRecordRepository.delete(alice);

        // Assert
        assertTrue(medicalRecords.isEmpty());
    }

}
