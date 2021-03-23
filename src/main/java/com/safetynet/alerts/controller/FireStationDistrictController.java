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

    /**
     * Endpoint to get all phone numbers of people living in the district of the fire stations with the given station number
     *
     * @param stationNumber
     *
     * @return A list of phone numbers as string
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhonesForFireStation(@RequestParam("firestation") int stationNumber){
        return fireStationDistrictService.getPhonesByStationNumber(stationNumber);
    }

    /**
     * Endpoint to get all persons and their medical information covered by fire stations with the given station number
     * Also return the count of adult and the count of child
     *
     * @param stationNumber
     *
     * @return FireStationDistrictDTO object that contains a list of persons information and the counters
     */
    @GetMapping("/firestation")
    public FireStationDistrictDTO getPersonsCoveredByFireStation(@RequestParam("stationNumber") int stationNumber){
        return fireStationDistrictService.getFireStationDistrictCoverage(stationNumber);
    }

    /**
     * Endpoint to get all persons and their medical information living at the given address
     * Also return the list of fire stations covering this address
     *
     * @param address
     * @return FireDTO object that contains a list of persons information and a list of fire station number
     */
    @GetMapping("/fire")
    public FireDTO getFireCoverageByAddress(@RequestParam("address") String address){
        return fireStationDistrictService.getFireInformationByAddress(address);
    }

    /**
     * Endpoint to get all persons and their medical information covered by fire stations with one of the given station number
     * Persons are grouped by address
     *
     * @param stationList containing the station numbers on whose the search is needed
     * @return FloodDTO object that contains a list of persons information where persons are grouped by address
     */
    @GetMapping("/flood/stations")
    public FloodDTO getFloodCoverageByStations(@RequestParam("stations") List<Integer> stationList){
        return fireStationDistrictService.getFloodInformationByStations(stationList);
    }
}
