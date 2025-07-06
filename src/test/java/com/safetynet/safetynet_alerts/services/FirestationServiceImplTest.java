package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.repositories.FirestationRepository;
import com.safetynet.safetynet_alerts.services.impl.FirestationServiceImpl;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirestationServiceImplTest {

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FirestationServiceImpl firestationService;


    @Test
    void retrieveAll_shouldReturnAllFirestations() {
        // Arrange
        List<Firestation> firestations = List.of(
                TestUtils.createFakeFirestation("10 Downing Street", 1)
        );
        when(firestationRepository.findAll()).thenReturn(firestations);

        // Act
        ResponseEntity<List<Firestation>> response = firestationService.retrieveAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firestations, response.getBody());
    }


    @Test
    void registerIfAbsent_shouldAddFirestation_whenNotExists() {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getStation(), dto.getAddress()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = firestationService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("La caserne de pompier a été ajoutée avec succès.", response.getBody());
    }


    @Test
    void registerIfAbsent_shouldReturnConflict_whenFirestationAlreadyExists() {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getStation(), dto.getAddress()))
                .thenReturn(Optional.of(dto.toFirestation()));

        // Act
        ResponseEntity<String> response = firestationService.registerIfAbsent(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("La caserne de pompier existe déjà.", response.getBody());
    }


    @Test
    void updateStationNumber_shouldUpdate_whenFirestationExists() {
        // Arrange
        UpdateFirestationDTO dto = TestUtils.createFakeUpdateFirestationDTO();
        Firestation existing = TestUtils.createFakeFirestation(dto.getAddress(), dto.getOldStation());
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getOldStation(), dto.getAddress()))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = firestationService.updateStationNumber(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La caserne de pompier a été mise à jour.", response.getBody());
    }


    @Test
    void updateStationNumber_shouldReturnNotFound_whenFirestationDoesNotExist() {
        // Arrange
        UpdateFirestationDTO dto = TestUtils.createFakeUpdateFirestationDTO();
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getOldStation(), dto.getAddress()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = firestationService.updateStationNumber(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("La caserne de pompier n'existe pas.", response.getBody());
    }


    @Test
    void delete_shouldRemoveFirestation_whenExists() {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();
        Firestation existing = dto.toFirestation();
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getStation(), dto.getAddress()))
                .thenReturn(Optional.of(existing));

        // Act
        ResponseEntity<String> response = firestationService.delete(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La caserne de pompier a été supprimée avec succès.", response.getBody());
    }


    @Test
    void delete_shouldReturnNotFound_whenFirestationDoesNotExist() {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();
        when(firestationRepository
                .findOptionalByStationNumberAndAddress(dto.getStation(), dto.getAddress()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = firestationService.delete(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("La caserne de pompier n'existe pas.", response.getBody());
    }


    @Test
    void getAddressesByStationNumber_shouldReturnMatchingAddresses() {
        // Arrange
        Firestation f1 = TestUtils.createFakeFirestation("10 Downing Street", 1);
        Firestation f2 = TestUtils.createFakeFirestation("36 quai des Orfèvres", 1);
        when(firestationRepository.findAllByStationNumber(1)).thenReturn(List.of(f1, f2));

        // Act
        Set<String> addresses = firestationService.getAddressesByStationNumber(1);

        // Assert
        assertEquals(Set.of("10 Downing Street", "36 quai des Orfèvres"), addresses);
    }


    @Test
    void getStationNumberByAddress_shouldReturnStation_whenExists() {
        // Arrange
        Firestation f = TestUtils.createFakeFirestation("10 Downing Street", 2);
        when(firestationRepository.findAll()).thenReturn(List.of(f));

        // Act
        Optional<Integer> result = firestationService.getStationNumberByAddress("10 Downing Street");

        // Assert
        assertEquals(Optional.of(2), result);
    }


    @Test
    void getAddressesByStationNumbers_shouldReturnAddresses() {
        // Arrange
        Firestation f1 = TestUtils.createFakeFirestation("10 Downing Street", 1);
        Firestation f2 = TestUtils.createFakeFirestation("36 quai des Orfèvres", 2);
        Firestation f3 = TestUtils.createFakeFirestation("Kremlin, Moscow", 3);
        when(firestationRepository.findAll()).thenReturn(List.of(f1, f2, f3));

        // Act
        Set<String> addresses = firestationService.getAddressesByStationNumbers(Set.of(1, 3));

        // Assert
        assertEquals(Set.of("10 Downing Street", "Kremlin, Moscow"), addresses);
    }

}
