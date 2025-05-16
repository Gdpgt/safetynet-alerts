package com.safetynet.safetynet_alerts.dto;

import lombok.Data;

@Data
public class UpdateFirestationDTO {

    private String address;
    private int oldStation;
    private int newStation;

}
