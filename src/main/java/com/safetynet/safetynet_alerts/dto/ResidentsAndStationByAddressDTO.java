package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResidentsAndStationByAddressDTO {

    private List<ResidentByAddressDTO> residents;
    private int firestationNumber;
}
