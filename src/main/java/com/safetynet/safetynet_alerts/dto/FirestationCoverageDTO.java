package com.safetynet.safetynet_alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FirestationCoverageDTO {

    List<PersonCoveredByFirestationDTO> coveredPersons;
    long numberOfAdults;
    long numberofChildren;
}
