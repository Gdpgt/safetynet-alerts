package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PhoneNumbersByFirestationNumberDTO {

    private Set<String> phoneNumbersByFirestationNumber;
}
