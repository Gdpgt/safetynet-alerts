package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.*;
import com.safetynet.safetynet_alerts.services.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }


    @GetMapping("/firestation")
    public ResponseEntity<FirestationCoverageDTO> getPersonsCoveredByFirestationNumber(@RequestParam int stationNumber) {
        return alertService.retrievePersonsCoveredByFirestationNumber(stationNumber);
    }


    @GetMapping("/childAlert")
    public ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> getChildrenAndFamilyMembersByAddress(@RequestParam String address) {
        return alertService.retrieveChildrenAndFamilyMembersByAddress(address);
    }


    @GetMapping("/phoneAlert")
    public ResponseEntity<PhoneNumbersByFirestationNumberDTO> getPhoneNumbersByFirestationNumber(@RequestParam int firestation) {
        return alertService.retrievePhoneNumbersByFirestationNumber(firestation);
    }


    @GetMapping("/fire")
    public ResponseEntity<ResidentsAndStationByAddressDTO> getResidentsAndStationByAddress(@RequestParam String address) {
        return alertService.retrieveResidentsAndStationByAddress(address);
    }


    @GetMapping("/flood/stations")
    public ResponseEntity<ResidentsByFirestationNumbersDTO> getResidentsByFirestationNumbers(@RequestParam Set<Integer> stations) {
        return alertService.retrieveResidentsByFirestationNumbers(stations);
    }


    @GetMapping("/personInfo")
    public ResponseEntity<List<ResidentByLastNameDTO>> getPersonsInfoByLastName(@RequestParam String lastName) {
        return alertService.retrievePersonsInfoByLastName(lastName);
    }


    @GetMapping("/communityEmail")
    public ResponseEntity<Set<String>> getEmailsByCity(@RequestParam String city) {
        return alertService.retrieveEmailsByCity(city);
    }

}
