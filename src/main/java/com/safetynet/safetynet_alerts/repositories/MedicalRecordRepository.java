package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepository {

    private final JsonDataLoader jsonDataLoader;

    public MedicalRecordRepository(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }


    public List<MedicalRecord> findAll() {
        return jsonDataLoader.getMedicalRecords();
    }


    public Optional<MedicalRecord> findOptionalByFirstAndLastName(String firstName, String lastName) {
        return jsonDataLoader.getMedicalRecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName)
                        && m.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }


    public void add(MedicalRecord medicalRecord) {
        jsonDataLoader.getMedicalRecords().add(medicalRecord);
        jsonDataLoader.saveData();
    }


    public void update(MedicalRecord existingMedicalRecord, MedicalRecord updatedData) {
        existingMedicalRecord.setBirthdate(updatedData.getBirthdate());
        existingMedicalRecord.setMedications(updatedData.getMedications());
        existingMedicalRecord.setAllergies(updatedData.getAllergies());
        jsonDataLoader.saveData();
    }


    public void delete(MedicalRecord existingMedicalRecord) {
        jsonDataLoader.getMedicalRecords().remove(existingMedicalRecord);
        jsonDataLoader.saveData();
    }

}
