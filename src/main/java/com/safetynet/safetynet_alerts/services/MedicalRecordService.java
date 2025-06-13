package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface MedicalRecordService {

    ResponseEntity<List<MedicalRecord>> retrieveAll();

    ResponseEntity<String> registerIfAbsent(MedicalRecordDTO dto);

    ResponseEntity<String> update(MedicalRecordDTO dto);

    ResponseEntity<String> delete(String firstName, String lastName);

    List<LocalDate> getBirthdatesOfPersons(Set<Person> coveredPersons);

    MedicalRecord getMedicalRecordByFirstAndLastName(String firstName, String lastName);
}
