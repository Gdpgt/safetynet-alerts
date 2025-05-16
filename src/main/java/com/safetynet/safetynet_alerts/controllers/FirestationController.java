package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.services.FirestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Firestation>> getAll() {
        return firestationService.retrieveAll();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody FirestationDTO dto) {
        return firestationService.registerIfAbsent(dto);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody UpdateFirestationDTO dto) {
        return firestationService.updateStationNumber(dto);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody FirestationDTO dto) {
        return firestationService.delete(dto);
    }

}
