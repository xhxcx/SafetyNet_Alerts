package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireStationDistrictDTO {

    private List<CoveredPersonDTO> coveredPersonDTOList;

    private int adultCount;

    private int childrenCount;

}
