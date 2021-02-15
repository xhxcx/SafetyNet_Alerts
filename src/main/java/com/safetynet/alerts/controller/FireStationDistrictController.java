package com.safetynet.alerts.controller;

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
}
