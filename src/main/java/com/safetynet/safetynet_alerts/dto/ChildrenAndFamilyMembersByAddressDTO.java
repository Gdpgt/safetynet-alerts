package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildrenAndFamilyMembersByAddressDTO {

    private List<ChildDTO> children;
    private List<FamilyMemberDTO> familyMembers;
}
