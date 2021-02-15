package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FireStationDistrictService {

    List<String> getPhonesByStationNumber(int stationNumber);
}
