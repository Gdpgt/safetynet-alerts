package com.safetynet.safetynet_alerts.repositories;

import com.safetynet.safetynet_alerts.data.JsonDataLoader;
import com.safetynet.safetynet_alerts.models.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FirestationRepository {

    private final JsonDataLoader jsonDataLoader;

    public FirestationRepository (JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }


    public List<Firestation> findAll() {
        return jsonDataLoader.getFirestations();
    }


    public Optional<Firestation> findOptionalByStationNumberAndAddress(int stationNumber, String address) {
        return jsonDataLoader.getFirestations().stream()
                .filter(f -> f.getStation() == stationNumber
                        && f.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }


    public void add(Firestation firestation) {
        jsonDataLoader.getFirestations().add(firestation);
    }


    public void updateStationNumber(Firestation existingFirestation, int newStationNumber) {
        existingFirestation.setStation(newStationNumber);
    }


    public void delete(Firestation existingFirestation) {
        jsonDataLoader.getFirestations().remove(existingFirestation);
    }


    public List<Firestation> findAllByStationNumber(int stationNumber) {
        return jsonDataLoader.getFirestations().stream()
                .filter(f -> f.getStation() == stationNumber)
                .toList();
    }

}
