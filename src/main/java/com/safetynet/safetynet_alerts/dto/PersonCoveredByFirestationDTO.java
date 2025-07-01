package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PersonCoveredByFirestationDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}
