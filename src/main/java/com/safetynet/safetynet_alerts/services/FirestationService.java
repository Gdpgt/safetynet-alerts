package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FirestationService {

    ResponseEntity<List<Firestation>> retrieveAll();

    ResponseEntity<String> registerIfAbsent(FirestationDTO dto);

    ResponseEntity<String> updateStationNumber(UpdateFirestationDTO dto);

    ResponseEntity<String> delete(FirestationDTO dto);

    Set<String> getAddressesByStationNumber(int stationNumber);

    Optional<Integer> getStationNumberByAddress(String address);

    Set<String> getAddressesByStationNumbers(Set<Integer> stationNumbers);
}
