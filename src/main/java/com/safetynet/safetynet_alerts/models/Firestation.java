package com.safetynet.safetynet_alerts.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"address", "station"})
@Builder
public class Firestation {

    private String address;
    private int station;
}
