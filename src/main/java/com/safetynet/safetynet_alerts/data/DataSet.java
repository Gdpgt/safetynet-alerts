package com.safetynet.safetynet_alerts.data;

import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import lombok.Data;

import java.util.List;

@Data
public class DataSet {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;

}
