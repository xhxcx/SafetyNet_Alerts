package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FireStationService {
    List<FireStation> getFireStations();

    FireStation saveFireStation(FireStation fireStation);

    FireStation updateFireStation(FireStation fireStation);

    boolean deleteFireStation(FireStation fireStation);

    FireStation getFireStationIfExists(FireStation fireStation);
}
