package com.safetynet.safetynet_alerts.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateFirestationDTO {

    private String address;
    private int oldStation;
    private int newStation;

}
