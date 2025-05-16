package com.safetynet.safetynet_alerts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
public class MedicalRecordDTO {

    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecord toMedicalRecord() {
        return new MedicalRecord(
                this.firstName,
                this.lastName,
                this.birthdate,
                this.medications,
                this.allergies
        );
    }
}
