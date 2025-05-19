package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FamilyMemberDTO {

    private String firstName;
    private String lastName;
    private int age;
}
