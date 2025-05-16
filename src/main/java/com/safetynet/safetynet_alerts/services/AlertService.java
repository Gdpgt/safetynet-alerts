package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.FirestationCoverageDTO;
import com.safetynet.safetynet_alerts.dto.PersonCoveredByFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.FirestationRepository;
import com.safetynet.safetynet_alerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final PersonRepository personRepository;

    private final FirestationRepository firestationRepository;

    private final MedicalRecordRepository medicalRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    public AlertService(PersonRepository personRepository, FirestationRepository firestationRepository,
                        MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }


    public ResponseEntity<FirestationCoverageDTO> retrievePersonsCoveredByFirestationNumber(int stationNumber) {
        List<String> addresses = getAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La caserne de pompier n°" + stationNumber + " n'existe pas.");
        }
        List<Person> coveredPersons = personRepository.findByAddresses(addresses);
        List<LocalDate> birthdates = getBirthdatesOfPersons(coveredPersons);
        long numberOfAdults = countNumberOfAdults(birthdates);
        long numberOfChildren = countNumberOfChildren(birthdates);
        List<PersonCoveredByFirestationDTO> coveredPersonsDTO = coveredPersons.stream()
                .map(p -> new PersonCoveredByFirestationDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone())).toList();
        return ResponseEntity.ok(new FirestationCoverageDTO(coveredPersonsDTO, numberOfAdults, numberOfChildren));
    }


    private List<String> getAddressesByStationNumber(int stationNumber) {
        List<Firestation> firestations = firestationRepository.findByStationNumber(stationNumber);
        return firestations.stream().map(Firestation::getAddress).toList();
    }


    private List<LocalDate> getBirthdatesOfPersons(List<Person> coveredPersons) {
        Set<String> fullNames = coveredPersons.stream().map(p -> p.getFirstName() + " " + p.getLastName()).collect(Collectors.toSet());
        return medicalRecordRepository.findAll().stream()
                .filter(m -> fullNames.contains(m.getFirstName() + " " + m.getLastName())).map(MedicalRecord::getBirthdate).toList();
    }


    private long countNumberOfAdults(List<LocalDate> birthdates) {
        return birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() > 18).count();
    }


    private long countNumberOfChildren(List<LocalDate> birthdates) {
        return birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() <= 18).count();
    }

}
