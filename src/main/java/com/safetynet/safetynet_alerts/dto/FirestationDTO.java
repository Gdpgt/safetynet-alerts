package com.safetynet.safetynet_alerts.dto;

import com.safetynet.safetynet_alerts.models.Firestation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirestationDTO {

    private String address;
    private int station;

    public Firestation toFirestation() {
        return new Firestation(this.address, this.station);
    }
}
