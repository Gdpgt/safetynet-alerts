package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.repositories.FirestationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;

    private static final Logger log = LoggerFactory.getLogger(FirestationService.class);

    FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }


    public ResponseEntity<List<Firestation>> retrieveAll() {
        List<Firestation> firestations = firestationRepository.findAll();
        return ResponseEntity.ok(firestations);
    }


    public ResponseEntity<String> registerIfAbsent(FirestationDTO dto) {
        Optional<Firestation> existingFirestation = firestationRepository.findByStationNumberAndAddress(dto.getStation(), dto.getAddress());

        if (existingFirestation.isEmpty()) {
            firestationRepository.add(dto.toFirestation());
            log.info("La caserne de pompier n°{}, située à l'adresse \"{}\", vient d'être ajoutée.", dto.getStation(), dto.getAddress());
            return ResponseEntity.status(HttpStatus.CREATED).body("La caserne de pompier a été ajoutée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La caserne de pompier existe déjà.");
        }
    }


    public ResponseEntity<String> updateStationNumber(UpdateFirestationDTO dto) {
        Optional<Firestation> existingFirestation = firestationRepository.findByStationNumberAndAddress(dto.getOldStation(), dto.getAddress());

        if (existingFirestation.isPresent()) {
            firestationRepository.updateStationNumber(existingFirestation.get(), dto.getNewStation());
            log.info("La caserne de pompier située à l'adresse \"{}\" vient d'être mise à jour avec le numéro de station {}", dto.getAddress(), dto.getNewStation());
            return ResponseEntity.ok("La caserne de pompier a été mise à jour.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La caserne de pompier n'existe pas.");
        }
    }


    public ResponseEntity<String> delete(FirestationDTO dto) {
        Optional<Firestation> existingFirestation = firestationRepository.findByStationNumberAndAddress(dto.getStation(), dto.getAddress());

        if (existingFirestation.isPresent()) {
            firestationRepository.delete(existingFirestation.get());
            log.info("La caserne de pompier n°{}, située à l'adresse \"{}\", vient d'être supprimée.", dto.getAddress(), dto.getStation());
            return ResponseEntity.ok("La caserne de pompier a été supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La caserne de pompier n'existe pas.");
        }
    }

}
