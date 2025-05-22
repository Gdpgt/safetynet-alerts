package com.safetynet.safetynet_alerts.dto;

import com.safetynet.safetynet_alerts.models.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResidentByAddressDTO {

    private String lastName;
    private String phone;
    private int age;
    private MedicationsAndAllergiesDTO medicationsAndAllergies;
}
