package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.repositories.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordService.class);

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }


    public ResponseEntity<List<MedicalRecord>> retrieveAll() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return ResponseEntity.ok(medicalRecords);
    }


    public ResponseEntity<String> registerIfAbsent(MedicalRecordDTO dto) {
        Optional<MedicalRecord> existingMedicalRecord = medicalRecordRepository.findByFirstAndLastName(dto.getFirstName(), dto.getLastName());

        if (existingMedicalRecord.isEmpty()) {
            medicalRecordRepository.add(dto.toMedicalRecord());
            log.info("Le dossier médical de la personne \"{} {}\" a été créé.", dto.getFirstName(), dto.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body("Le dossier médical a été ajouté avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Le dossier médical existe déjà.");
        }
    }


    public ResponseEntity<String> update(MedicalRecordDTO dto) {
        Optional<MedicalRecord> existingMedicalRecord = medicalRecordRepository.findByFirstAndLastName(dto.getFirstName(), dto.getLastName());

        if (existingMedicalRecord.isPresent()) {
            medicalRecordRepository.update(existingMedicalRecord.get(), dto.toMedicalRecord());
            log.info("Le dossier médical de la personne \"{} {}\" a été mis à jour.", dto.getFirstName(), dto.getLastName());
            return ResponseEntity.ok("Le dossier médical a été mis à jour.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le dossier médical n'existe pas.");
        }
    }


    public ResponseEntity<String> delete(String firstName, String lastName) {
        Optional<MedicalRecord> existingMedicalRecord = medicalRecordRepository.findByFirstAndLastName(firstName, lastName);

        if (existingMedicalRecord.isPresent()) {
            medicalRecordRepository.delete(existingMedicalRecord.get());
            log.info("Le dossier médical de la personne \"{} {}\" vient d'être supprimé.", firstName, lastName);
            return ResponseEntity.ok("Le dossier médical a été supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le dossier médical n'existe pas.");
        }
    }

}
