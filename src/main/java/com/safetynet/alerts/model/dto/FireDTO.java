package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireDTO {

    private List<DisasterVictimDTO> victimList;

    private List<Integer> stationNumberList;
}
