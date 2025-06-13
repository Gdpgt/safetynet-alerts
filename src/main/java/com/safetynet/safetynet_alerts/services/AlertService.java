package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface AlertService {

    ResponseEntity<FirestationCoverageDTO> retrievePersonsCoveredByFirestationNumber(int stationNumber);

    ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> retrieveChildrenAndFamilyMembersByAddress(String address);

    ResponseEntity<PhoneNumbersByFirestationNumberDTO> retrievePhoneNumbersByFirestationNumber(int firestationNumber);

    ResponseEntity<ResidentsAndStationByAddressDTO> retrieveResidentsAndStationByAddress(String address);

    ResponseEntity<ResidentsByFirestationNumbersDTO> retrieveResidentsByFirestationNumbers(Set<Integer> stationNumbers);

    ResponseEntity<List<ResidentByLastNameDTO>> retrievePersonsInfoByLastName(String lastName);

    ResponseEntity<Set<String>> retrieveEmailsByCity(String city);
}
