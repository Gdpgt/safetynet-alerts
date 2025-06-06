package com.safetynet.safetynet_alerts.mappers;

import com.safetynet.safetynet_alerts.dto.MedicationsAndAllergiesDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;

public class MedicalRecordMapper {

    public static MedicationsAndAllergiesDTO getMedicationsAndAllergiesByMedicalRecord(MedicalRecord medicalRecord) {
        return new MedicationsAndAllergiesDTO(medicalRecord.getMedications(), medicalRecord.getAllergies());
    }
}
