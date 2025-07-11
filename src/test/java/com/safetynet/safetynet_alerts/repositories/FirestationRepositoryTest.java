package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Firestation;
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
public class FirestationRepositoryTest {

    @Mock
    JsonDataLoader jsonDataLoader;

    @InjectMocks
    FirestationRepository firestationRepository;


    @Test
    void findAll_shouldReturnAllFirestations() {
        // Arrange
        List<Firestation> stations = List.of(
                TestUtils.createFakeFirestation("10 Downing Street", 1),
                TestUtils.createFakeFirestation("36 quai des Orfèvres", 2)
        );
        when(jsonDataLoader.getFirestations()).thenReturn(stations);

        // Act
        List<Firestation> result = firestationRepository.findAll();

        // Assert
        assertEquals(stations, result);
    }


    @Test
    void findOptionalByStationNumberAndAddress_shouldReturnFirestation_whenExists() {
        // Arrange
        Firestation fs = TestUtils.createFakeFirestation("10 Downing Street", 1);
        when(jsonDataLoader.getFirestations()).thenReturn(List.of(fs));

        // Act
        Optional<Firestation> result = firestationRepository.findOptionalByStationNumberAndAddress(1, "10 Downing Street");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fs, result.get());
    }


    @Test
    void add_shouldAppendFirestation() {
        // Arrange
        List<Firestation> stations = new ArrayList<>();
        when(jsonDataLoader.getFirestations()).thenReturn(stations);
        Firestation newStation = TestUtils.createFakeFirestation("10 Downing Street", 3);

        // Act
        firestationRepository.add(newStation);

        // Assert
        assertEquals(1, stations.size());
        assertTrue(stations.contains(newStation));
    }


    @Test
    void updateStationNumber_shouldModifyField() {
        // Arrange
        Firestation existing = TestUtils.createFakeFirestation("36 quai des Orfèvres", 1);

        // Act
        firestationRepository.updateStationNumber(existing, 4);

        // Assert
        assertEquals(4, existing.getStation());
    }


    @Test
    void delete_shouldRemoveFirestation() {
        // Arrange
        Firestation fs = TestUtils.createFakeFirestation("10 Downing Street", 2);
        List<Firestation> stations = new ArrayList<>(List.of(fs));
        when(jsonDataLoader.getFirestations()).thenReturn(stations);

        // Act
        firestationRepository.delete(fs);

        // Assert
        assertTrue(stations.isEmpty());
    }


    @Test
    void findAllByStationNumber_shouldReturnMatchingFirestations() {
        // Arrange
        Firestation fs1 = TestUtils.createFakeFirestation("10 Downing Street", 1);
        Firestation fs2 = TestUtils.createFakeFirestation("36 quai des Orfèvres", 2);
        when(jsonDataLoader.getFirestations()).thenReturn(List.of(fs1, fs2));

        // Act
        List<Firestation> result = firestationRepository.findAllByStationNumber(1);

        // Assert
        assertEquals(List.of(fs1), result);
    }

}
