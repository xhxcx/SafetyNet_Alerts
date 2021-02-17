package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FireStationDistrictDTO;
import com.safetynet.alerts.model.dto.FloodDTO;
import com.safetynet.alerts.service.FireStationDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FireStationDistrictController {

    @Autowired
    private FireStationDistrictService fireStationDistrictService;


    @GetMapping("/phoneAlert")
    public List<String> getPhonesForFireStation(@RequestParam("firestation") int stationNumber){
        return fireStationDistrictService.getPhonesByStationNumber(stationNumber);
    }

    @GetMapping("/firestation")
    public FireStationDistrictDTO getPersonsCoveredByFireStation(@RequestParam("stationNumber") int stationNumber){
        return fireStationDistrictService.getFireStationDistrictCoverage(stationNumber);
    }

    @GetMapping("/fire")
    public FireDTO getFireCoverageByAddress(@RequestParam("address") String address){
        return fireStationDistrictService.getFireInformationByAddress(address);
    }

    @GetMapping("/flood/stations")
    public FloodDTO getFloodCoverageByStations(@RequestParam("stations") List<Integer> stationList){
        return fireStationDistrictService.getFloodInformationByStations(stationList);
    }
}
