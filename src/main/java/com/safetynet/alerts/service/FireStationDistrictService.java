package com.safetynet.alerts.service;

import com.safetynet.alerts.model.dto.FireStationDistrictDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FireStationDistrictService {

    List<String> getPhonesByStationNumber(int stationNumber);

    FireStationDistrictDTO getFireStationDistrictCoverage(int stationNumber);
}
