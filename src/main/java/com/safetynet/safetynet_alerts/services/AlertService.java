package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Set<String> addresses = getAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            log.info("La caserne de pompier n°{} n'existe pas.", stationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Set<Person> coveredPersons = personRepository.findByAddresses(addresses);
        List<LocalDate> birthdates = getBirthdatesOfPersons(coveredPersons);
        long numberOfAdults = countNumberOfAdults(birthdates);
        long numberOfChildren = countNumberOfChildren(birthdates);
        List<PersonCoveredByFirestationDTO> coveredPersonsDTO = coveredPersons.stream()
                .map(p -> new PersonCoveredByFirestationDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone())).toList();
        log.info("Les personnes liées à la caserne de pompier n°{} ont été récupérées", stationNumber);
        return ResponseEntity.ok(new FirestationCoverageDTO(coveredPersonsDTO, numberOfAdults, numberOfChildren));
    }


    private Set<String> getAddressesByStationNumber(int stationNumber) {
        List<Firestation> firestations = firestationRepository.findByStationNumber(stationNumber);
        return firestations.stream().map(Firestation::getAddress).collect(Collectors.toSet());
    }


    private List<LocalDate> getBirthdatesOfPersons(Set<Person> coveredPersons) {
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


    public ResponseEntity<ChildrenAndFamilyMembersByAddressDTO> retrieveChildrenAndFamilyMembersByAddress(String address) {
        List<Person> personsFromAddress = personRepository.findByAddress(address);

        if (personsFromAddress.isEmpty()) {
            log.info("Personne n'habite à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<String> fullnames = getFullNamesOfPersons(personsFromAddress);
        List<MedicalRecord> medicalRecordsOfChildren = medicalRecordRepository.findAll().stream().filter(m -> fullnames.contains(m.getFirstName() + " " + m.getLastName()) && calculateAge(m.getBirthdate()) <= 18).toList();

        if (medicalRecordsOfChildren.isEmpty()) {
            log.info("Il n'y a pas d'enfant à l'adresse \"{}\".", address);
            return ResponseEntity.ok().body(null);
        }

        List<MedicalRecord> medicalRecordsOfAdults = medicalRecordRepository.findAll().stream().filter(m -> fullnames.contains(m.getFirstName() + " " + m.getLastName()) && calculateAge(m.getBirthdate()) > 18).toList();
        List<ChildDTO> childrenFromAddress = medicalRecordsOfChildren.stream().map(m -> new ChildDTO(m.getFirstName(), m.getLastName(), calculateAge(m.getBirthdate()))).toList();
        List<FamilyMemberDTO> familyMembers = medicalRecordsOfAdults.stream().map(m -> new FamilyMemberDTO(m.getFirstName(), m.getLastName(), calculateAge(m.getBirthdate()))).toList();
        log.info("Les enfants et membres du foyer de l'adresse \"{}\" ont été récupérés.", address);
        return ResponseEntity.ok(new ChildrenAndFamilyMembersByAddressDTO(childrenFromAddress, familyMembers));
    }


    private Set<String> getFullNamesOfPersons(List<Person> persons) {
        return persons.stream().map(p -> p.getFirstName() + " " + p.getLastName()).collect(Collectors.toSet());
    }


    private int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }


    public ResponseEntity<PhoneNumbersByFirestationNumberDTO> retrievePhoneNumbersByFirestationNumber(int firestationNumber) {
        Set<String> addressesByFirestationNumber = getAddressesByStationNumber(firestationNumber);

        if (addressesByFirestationNumber.isEmpty()) {
            log.info("La caserne de pompier n°{} n'existe pas.", firestationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<String> phoneNumbers = personRepository.findByAddresses(addressesByFirestationNumber).stream()
                .map(Person::getPhone).collect(Collectors.toSet());

        if (phoneNumbers.isEmpty()) {
            log.info("Aucune personne n'est rattachée aux adresses \"{}\".", addressesByFirestationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les numéros de téléphone rattachés à la caserne de pompier n°{} ont été récupérés.", firestationNumber);
        return ResponseEntity.ok(new PhoneNumbersByFirestationNumberDTO(phoneNumbers));
    }


    public ResponseEntity<ResidentsAndStationByAddressDTO> retrieveResidentsAndStationByAddress(String address) {
        Optional<Integer> stationNumber = getStationNumberByAddress(address);

        if (stationNumber.isEmpty()) {
            log.info("Aucune caserne de pompier n'existe à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Person> personsByAddress = personRepository.findByAddress(address);

        if (personsByAddress.isEmpty()) {
            log.info("Aucune personne n'habite à l'adresse \"{}\".", address);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("Les habitants de l'adresse \"{}\" et le numéro de la caserne de pompier y étant rattachée ont été récupérés.", address);
        return ResponseEntity.ok(new ResidentsAndStationByAddressDTO(createResidentsByAddressDTO(personsByAddress), stationNumber.get()));
    }


    private Optional<Integer> getStationNumberByAddress(String address) {
        return firestationRepository.findAll().stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .findFirst();
    }


    private List<ResidentByAddressDTO> createResidentsByAddressDTO(List<Person> persons) {
        return persons.stream()
                .map(p -> {
                    MedicalRecord medicalRecord = getMedicalRecordByFirstAndLastName(p.getFirstName(), p.getLastName());
                    return new ResidentByAddressDTO(p.getLastName(), p.getPhone()
                            , calculateAge(medicalRecord.getBirthdate())
                            , getMedicationsAndAllergiesByMedicalRecord(medicalRecord));
                })
                .toList();
    }


    private MedicalRecord getMedicalRecordByFirstAndLastName(String firstName, String lastName) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findAll().stream()
                .filter(m -> (m.getFirstName() + " " + m.getLastName()).equalsIgnoreCase(firstName + " " + lastName))
                .findFirst();

        if (medicalRecord.isEmpty()) {
            log.warn("La personne \"{}\" liée à l'adresse n'a pas de dossier médical.", firstName + " " + lastName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return medicalRecord.get();
    }


    private MedicationsAndAllergiesDTO getMedicationsAndAllergiesByMedicalRecord(MedicalRecord medicalRecord) {
        return new MedicationsAndAllergiesDTO(medicalRecord.getMedications(), medicalRecord.getAllergies());
    }


    public ResponseEntity<ResidentsByFirestationNumbersDTO> retrieveResidentsByFirestationNumbers(Set<Integer> stationNumbers) {
        Set<String> addresses = getAddressesByStationNumbers(stationNumbers);

        if (addresses.isEmpty()) {
            log.warn("Le(s) numéro(s) {} de caserne de pompier n'existe(nt) pas.", stationNumbers);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<AddressAndItsResidentsDTO> addressesAndTheirResidentsDTO = addresses.stream()
                .map(this::buildAddressResidentsDTO)
                .toList();

        log.info("Les adresses et résidents de la ou des casernes de pompier n°{} ont été récupérés.", stationNumbers);
        return ResponseEntity.ok(new ResidentsByFirestationNumbersDTO(addressesAndTheirResidentsDTO));
    }


    private Set<String> getAddressesByStationNumbers(Set<Integer> stationNumbers) {
        return firestationRepository.findAll().stream()
                .filter(f -> stationNumbers.contains(f.getStation()))
                .map(Firestation::getAddress).collect(Collectors.toSet());
    }


    private AddressAndItsResidentsDTO buildAddressResidentsDTO(String address) {
        List<Person> residents = personRepository.findByAddress(address);

        if (residents.isEmpty()) {
            log.warn("Personne n'habite à l'adresse \"{}\".", address);
            return new AddressAndItsResidentsDTO(address, Collections.emptyList());
        }

        return new AddressAndItsResidentsDTO(address, createResidentsByAddressDTO(residents));
    }

}
