package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FirestationRepository {

    private final JsonDataLoader jsonDataLoader;

    public FirestationRepository (JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<Firestation> findAll() {
        return jsonDataLoader.getFirestations();
    }

}
