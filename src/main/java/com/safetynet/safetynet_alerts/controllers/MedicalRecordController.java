package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.services.MedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<MedicalRecord>> getAll() {
        return medicalRecordService.retrieveAll();
    }


    @PostMapping
    public ResponseEntity<String> add(@RequestBody MedicalRecordDTO dto) {
        return medicalRecordService.registerIfAbsent(dto);
    }


    @PutMapping
    public ResponseEntity<String> update(@RequestBody MedicalRecordDTO dto) {
        return medicalRecordService.update(dto);
    }


    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String firstName, @RequestParam String lastName) {
        return medicalRecordService.delete(firstName, lastName);
    }

}
