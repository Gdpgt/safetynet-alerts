package com.safetynet.safetynet_alerts.data;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.models.Person;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Component
@Getter
public class JsonDataLoader {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;

    private static final Logger log = LoggerFactory.getLogger(JsonDataLoader.class);

    @PostConstruct
    public void loadJson() {

        try {
            File input = new File("src/main/data.json");

            ObjectMapper mapper = new ObjectMapper();
            DataSet dt = mapper.readValue(input, DataSet.class);

            this.persons = dt.getPersons();
            this.firestations = dt.getFirestations();
            this.medicalRecords = dt.getMedicalRecords();

        } catch (FileNotFoundException e) {
            log.error("Le fichier est introuvable.", e);
            throw new IllegalStateException("L'application ne peut pas démarrer sans data.json.", e);
        } catch (JsonMappingException | JsonParseException e) {
            log.error("Le format du json est incorrect.", e);
            throw new IllegalStateException("L'application ne peut pas démarrer si data.json n'est pas au bon format.", e);
        } catch (IOException e) {
            log.error("Échec du chargement de data.json", e);
            throw new IllegalStateException("L'application ne peut pas démarrer sans chargement correct du data.json.", e);
        }

    }
}
