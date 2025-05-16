package com.safetynet.safetynet_alerts.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"address", "station"})
public class Firestation {

    private String address;
    private int station;
}
