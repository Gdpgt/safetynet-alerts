package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.ChildrenAndFamilyMembersByAddressDTO;
import com.safetynet.safetynet_alerts.dto.FirestationCoverageDTO;
import com.safetynet.safetynet_alerts.dto.PhoneNumbersByFirestationNumberDTO;
import com.safetynet.safetynet_alerts.dto.ResidentsAndStationByAddressDTO;
import com.safetynet.safetynet_alerts.services.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
