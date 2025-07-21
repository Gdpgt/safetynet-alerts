package com.safetynet.safetynet_alerts.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class JsonDataLoader {

    private static final Logger log = LoggerFactory.getLogger(JsonDataLoader.class);
    private static final Path DATA_PATH = Paths.get("src/main/resources/data.json");

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private List<Person> persons = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();


    @PostConstruct
    public void loadJson() {
        try {
            DataSet dt = mapper.readValue(DATA_PATH.toFile(), DataSet.class);
            persons = new ArrayList<>(dt.getPersons());
            firestations = new ArrayList<>(dt.getFirestations());
            medicalRecords = new ArrayList<>(dt.getMedicalRecords());
        } catch (IOException e) {
            log.error("Impossible de charger {}", DATA_PATH.toAbsolutePath(), e);
            throw new IllegalStateException("L'application ne peut pas démarrer sans data.json.", e);
        }
    }


    public synchronized void saveData() {
        try {
            DataSet dt = new DataSet(persons, firestations, medicalRecords);
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(DATA_PATH.toFile(), dt);
            log.debug("data.json mis à jour.");
        } catch (IOException e) {
            log.error("Échec de la persistance de data.json", e);
            throw new IllegalStateException("Écriture impossible dans data.json.", e);
        }
    }
}
