package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.*;
import com.safetynet.safetynet_alerts.assemblers.ResidentAssembler;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.MedicalRecordRepository;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import com.safetynet.safetynet_alerts.utils.AgeUtils;
import com.safetynet.safetynet_alerts.utils.DateUtils;
import com.safetynet.safetynet_alerts.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final PersonRepository personRepository;

    private final FirestationService firestationService;

    private final MedicalRecordService medicalRecordService;

    private final MedicalRecordRepository medicalRecordRepository;

    private final ResidentAssembler residentAssembler;

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    public AlertService(PersonRepository personRepository,
                        FirestationService firestationService,
                        MedicalRecordService medicalRecordService,
                        MedicalRecordRepository medicalRecordRepository,
                        ResidentAssembler residentAssembler) {
        this.personRepository = personRepository;
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
        this.medicalRecordRepository = medicalRecordRepository;
        this.residentAssembler = residentAssembler;
    }


    public ResponseEntity<FirestationCoverageDTO> retrievePersonsCoveredByFirestationNumber(int stationNumber) {
        Set<String> addresses = firestationService.getAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            log.warn("La caserne de pompier n°{} n'existe pas.", stationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Set<Person> coveredPersons = personRepository.findAllByAddresses(addresses);
        List<LocalDate> birthdates = medicalRecordService.getBirthdatesOfPersons(coveredPersons);
        long numberOfAdults = AgeUtils.countNumberOfAdults(birthdates);
        long numberOfChildren = AgeUtils.countNumberOfChildren(birthdates);
        List<PersonCoveredByFirestationDTO> coveredPersonsDTO = coveredPersons.stream()
                .map(p -> new PersonCoveredByFirestationDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone())).toList();
        log.info("Les personnes liées à la caserne de pompier n°{} ont été récupérées", stationNumber);
        return ResponseEntity.ok(new FirestationCoverageDTO(coveredPersonsDTO, numberOfAdults, numberOfChildren));
    }


    public ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> retrieveChildrenAndFamilyMembersByAddress(String address) {
        List<Person> personsFromAddress = personRepository.findAllByAddress(address);

        if (personsFromAddress.isEmpty()) {
            log.warn("Personne n'habite à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<String> fullnames = NameUtils.getFullNamesOfPersons(personsFromAddress);
        List<MedicalRecord> medicalRecordsOfChildren = medicalRecordRepository.findAll().stream().filter(m -> fullnames.contains(m.getFirstName() + " " + m.getLastName()) && DateUtils.calculateAge(m.getBirthdate()) <= 18).toList();

        if (medicalRecordsOfChildren.isEmpty()) {
            log.info("Il n'y a pas d'enfant à l'adresse \"{}\".", address);
            return ResponseEntity.ok().body(null);
        }

        List<MedicalRecord> medicalRecordsOfAdults = medicalRecordRepository.findAll().stream().filter(m -> fullnames.contains(m.getFirstName() + " " + m.getLastName()) && DateUtils.calculateAge(m.getBirthdate()) > 18).toList();
        List<ChildDTO> childrenFromAddress = medicalRecordsOfChildren.stream().map(m -> new ChildDTO(m.getFirstName(), m.getLastName(), DateUtils.calculateAge(m.getBirthdate()))).toList();
        List<FamilyMemberDTO> familyMembers = medicalRecordsOfAdults.stream().map(m -> new FamilyMemberDTO(m.getFirstName(), m.getLastName(), DateUtils.calculateAge(m.getBirthdate()))).toList();
        log.info("Les enfants et membres du foyer de l'adresse \"{}\" ont été récupérés.", address);
        return ResponseEntity.ok(new ChildrenAndFamilyMembersByAddressDTO(childrenFromAddress, familyMembers));
    }


    public ResponseEntity<PhoneNumbersByFirestationNumberDTO> retrievePhoneNumbersByFirestationNumber(int firestationNumber) {
        Set<String> addressesByFirestationNumber = firestationService.getAddressesByStationNumber(firestationNumber);

        if (addressesByFirestationNumber.isEmpty()) {
            log.warn("La caserne de pompier n°{} n'existe pas.", firestationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<String> phoneNumbers = personRepository.findAllByAddresses(addressesByFirestationNumber).stream()
                .map(Person::getPhone).collect(Collectors.toSet());

        if (phoneNumbers.isEmpty()) {
            log.warn("Aucune personne n'est rattachée aux adresses \"{}\".", addressesByFirestationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les numéros de téléphone rattachés à la caserne de pompier n°{} ont été récupérés.", firestationNumber);
        return ResponseEntity.ok(new PhoneNumbersByFirestationNumberDTO(phoneNumbers));
    }


    public ResponseEntity<ResidentsAndStationByAddressDTO> retrieveResidentsAndStationByAddress(String address) {
        Optional<Integer> stationNumber = firestationService.getStationNumberByAddress(address);

        if (stationNumber.isEmpty()) {
            log.warn("Aucune caserne de pompier n'existe à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Person> personsByAddress = personRepository.findAllByAddress(address);

        if (personsByAddress.isEmpty()) {
            log.warn("Aucune personne n'habite à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les habitants de l'adresse \"{}\" et le numéro de la caserne de pompier y étant rattachée ont été récupérés.", address);
        return ResponseEntity.ok(new ResidentsAndStationByAddressDTO(residentAssembler.createResidentsByAddressDTO(personsByAddress), stationNumber.get()));
    }


    public ResponseEntity<ResidentsByFirestationNumbersDTO> retrieveResidentsByFirestationNumbers(Set<Integer> stationNumbers) {
        Set<String> addresses = firestationService.getAddressesByStationNumbers(stationNumbers);

        if (addresses.isEmpty()) {
            log.warn("Le(s) numéro(s) {} de caserne de pompier n'existe(nt) pas.", stationNumbers);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<AddressAndItsResidentsDTO> addressesAndTheirResidentsDTO = addresses.stream()
                .map(residentAssembler::buildAddressResidentsDTO)
                .toList();

        log.info("Les adresses et résidents de la ou des casernes de pompier n°{} ont été récupérés.", stationNumbers);
        return ResponseEntity.ok(new ResidentsByFirestationNumbersDTO(addressesAndTheirResidentsDTO));
    }


    public ResponseEntity<List<ResidentByLastNameDTO>> retrievePersonsInfoByLastName(String lastName) {
        List<Person> persons = personRepository.findAllByLastName(lastName);

        if (persons.isEmpty()) {
            log.warn("Il n'existe pas de personne au nom de \"{}\".", lastName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les infos des personnes liées au nom de famille \"{}\" ont été récupérées.", lastName);
        return ResponseEntity.ok(residentAssembler.createResidentsByLastNameDTO(persons));
    }


    public ResponseEntity<Set<String>> retrieveEmailsByCity(String city) {
        List<Person> persons = personRepository.findAllByCity(city);

        if (persons.isEmpty()) {
            log.warn("Aucune personne n'est enregistrée comme habitant dans la ville de {}", city);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les adresses email des habitants de la ville de {} ont été récupérées.", city);
        return ResponseEntity.ok(persons.stream().map(Person::getEmail).collect(Collectors.toSet()));
    }

}
