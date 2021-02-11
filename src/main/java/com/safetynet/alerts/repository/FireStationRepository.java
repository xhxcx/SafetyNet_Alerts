package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.utils.AlertsDataOutputWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FireStationRepository {
    private static final Logger log = LogManager.getLogger(FireStationRepository.class);

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<FireStation> getFireStations() {
        log.info("Getting all fire stations from the file !");
        return getAlertsData.getFirestations();
    }

    public FireStation createFireStation(FireStation fireStation) {
        log.debug("Try to create new fire station");
        getAlertsData.getFirestations().add(fireStation);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData, outputFilePath);
        log.info("New fire station created with following informations :" + fireStation);
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        log.debug("Try to update fire station : " + fireStation.getAddress() + " " + fireStation.getStation());
        FireStation fireStationToUpdate = getFireStations().stream()
                .filter((fs)->fs.getAddress().equalsIgnoreCase(fireStation.getAddress())&&fs.getStation()==fireStation.getStation())
                .findAny()
                .orElse(null);
        if(null != fireStationToUpdate){
            getAlertsData.getFirestations().set(getAlertsData.getFirestations().indexOf(fireStationToUpdate),fireStation);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData, outputFilePath);
            log.info(fireStation.getAddress() + " " + fireStation.getStation() + " updated !");
        }
        return fireStation;
    }

    public void deleteFireStation(FireStation fireStation) {
        log.debug("Try to delete fire station : " + fireStation.getAddress() + " " + fireStation.getStation());
        getAlertsData.getFirestations().remove(fireStation);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        log.info(fireStation.getAddress() + " " + fireStation.getStation() + " deleted from fire stations !");
    }
}
