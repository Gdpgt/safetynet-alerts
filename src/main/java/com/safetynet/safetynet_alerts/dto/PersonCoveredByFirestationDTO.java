package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonCoveredByFirestationDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}
