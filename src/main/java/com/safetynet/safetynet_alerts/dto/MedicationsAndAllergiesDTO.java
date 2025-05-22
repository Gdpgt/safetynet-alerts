package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MedicationsAndAllergiesDTO {

    private List<String> medications;
    private List<String> allergies;
}
