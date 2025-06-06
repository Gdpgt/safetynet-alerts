package com.safetynet.safetynet_alerts.assemblers;

import com.safetynet.safetynet_alerts.dto.AddressAndItsResidentsDTO;
import com.safetynet.safetynet_alerts.dto.ResidentByAddressDTO;
import com.safetynet.safetynet_alerts.dto.ResidentByLastNameDTO;
import com.safetynet.safetynet_alerts.mappers.MedicalRecordMapper;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.repositories.PersonRepository;
import com.safetynet.safetynet_alerts.services.MedicalRecordService;
import com.safetynet.safetynet_alerts.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ResidentAssembler {

    private final MedicalRecordService medicalRecordService;

    private final PersonRepository personRepository;

    private static final Logger log = LoggerFactory.getLogger(ResidentAssembler.class);

    public ResidentAssembler(MedicalRecordService medicalRecordService, PersonRepository personRepository) {
        this.medicalRecordService = medicalRecordService;
        this.personRepository = personRepository;
    }


    public List<ResidentByAddressDTO> createResidentsByAddressDTO(List<Person> persons) {
        return persons.stream()
                .map(p -> {
                    MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFirstAndLastName(p.getFirstName(), p.getLastName());
                    return new ResidentByAddressDTO(p.getLastName(), p.getPhone()
                            , DateUtils.calculateAge(medicalRecord.getBirthdate())
                            , MedicalRecordMapper.getMedicationsAndAllergiesByMedicalRecord(medicalRecord));
                })
                .toList();
    }


    public AddressAndItsResidentsDTO buildAddressResidentsDTO(String address) {
        List<Person> residents = personRepository.findByAddress(address);

        if (residents.isEmpty()) {
            log.warn("Personne n'habite à l'adresse \"{}\".", address);
            return new AddressAndItsResidentsDTO(address, Collections.emptyList());
        }

        return new AddressAndItsResidentsDTO(address, createResidentsByAddressDTO(residents));
    }


    public List<ResidentByLastNameDTO> createResidentsByLastNameDTO(List<Person> persons) {
        return persons.stream()
                .map(p -> {
                    MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByFirstAndLastName(p.getFirstName(), p.getLastName());
                    return new ResidentByLastNameDTO(p.getLastName(), p.getAddress()
                            , DateUtils.calculateAge(medicalRecord.getBirthdate())
                            , p.getEmail()
                            , MedicalRecordMapper.getMedicationsAndAllergiesByMedicalRecord(medicalRecord));
                })
                .toList();
    }
}
