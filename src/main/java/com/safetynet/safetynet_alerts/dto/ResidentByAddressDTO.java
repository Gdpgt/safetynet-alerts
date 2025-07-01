package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResidentByAddressDTO {

    private String lastName;
    private String phone;
    private int age;
    private MedicationsAndAllergiesDTO medicationsAndAllergies;
}
