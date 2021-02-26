package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.utils.AlertsDataOutputWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FireStationRepository {

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<FireStation> getFireStations() {
        return getAlertsData.getFirestations();
    }

    public FireStation createFireStation(FireStation fireStation) {
        getAlertsData.getFirestations().add(fireStation);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData, outputFilePath);
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        FireStation fireStationToUpdate = getFireStations().stream()
                .filter((fs)->fs.getAddress().equalsIgnoreCase(fireStation.getAddress())&&fs.getStation()==fireStation.getStation())
                .findAny()
                .orElse(null);
        if(fireStationToUpdate != null){
            getAlertsData.getFirestations().set(getAlertsData.getFirestations().indexOf(fireStationToUpdate),fireStation);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData, outputFilePath);
        }
        return fireStation;
    }

    public void deleteFireStation(FireStation fireStation) {
        getAlertsData.getFirestations().remove(fireStation);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
    }
}
