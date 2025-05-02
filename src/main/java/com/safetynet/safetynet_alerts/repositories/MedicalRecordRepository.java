package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final JsonDataLoader jsonDataLoader;

    MedicalRecordRepository(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<MedicalRecord> findAll() {
        return jsonDataLoader.getMedicalRecords();
    }
}
