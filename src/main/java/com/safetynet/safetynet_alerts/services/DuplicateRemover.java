package com.safetynet.safetynet_alerts.services;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DuplicateRemover {

    private final JsonDataLoader jsonDataLoader;

    private static final Logger log = LoggerFactory.getLogger(DuplicateRemover.class);

    DuplicateRemover(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }


    @PostConstruct
    public void cleanDuplicates() {
        jsonDataLoader.setPersons(removeDuplicates(jsonDataLoader.getPersons()));
        jsonDataLoader.setFirestations(removeDuplicates(jsonDataLoader.getFirestations()));
        jsonDataLoader.setMedicalRecords(removeDuplicates(jsonDataLoader.getMedicalRecords()));

        log.info("Les doublons ont été supprimés des données source.");
    }

    private <T> List<T>  removeDuplicates(List<T> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
