package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResidentByLastNameDTO {

    private String lastName;
    private String address;
    private int age;
    private String email;
    private MedicationsAndAllergiesDTO medicationsAndAllergiesDTO;

}
