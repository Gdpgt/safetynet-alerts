package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddressAndItsResidentsDTO {

    private String address;
    private List<ResidentByAddressDTO> residents;
}
