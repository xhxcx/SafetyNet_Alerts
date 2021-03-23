package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationServiceImpl implements FireStationService {

    private static final Logger log = LogManager.getLogger(FireStationService.class);

    private static FireStation fireStationToProcess = null;

    @Autowired
    private FireStationRepository fireStationRepository;

    /**
     * Get all fire stations from the data entry
     * @return a list of FireStation objects
     */
    @Override
    public List<FireStation> getFireStations() {
        return fireStationRepository.getFireStations();
    }

    /**
     * Save a new FireStation
     * If the fire station already exists return null and log error to prevent duplication
     *
     * @param fireStation to add
     * @return saved fire station or null if station already exists or is null
     */
    @Override
    public FireStation saveFireStation(FireStation fireStation) {
        if(fireStation != null){
            fireStationToProcess = getFireStationIfExists(fireStation);
            if (fireStationToProcess == null){
                fireStationToProcess = fireStationRepository.createFireStation(fireStation);
                log.info("New fire station created with following information :" + fireStation);
            }
            else {
                fireStationToProcess = null;
                log.error("FireStation creation failed :: FireStation " + fireStation + " already exists");
            }
        }
        else
            log.error("FireStation creation failed :: Can't create a null fire station");
        return fireStationToProcess;
    }

    /**
     * Modify an existing FireStation
     * Log error if given fire station doesn't exists in data list or is null
     * @param fireStation to update
     * @return Fire station to update if update ok, else return null and log error
     */
    @Override
    public FireStation updateFireStation(FireStation fireStation) {
        if(fireStation != null){
            fireStationToProcess = getFireStationIfExists(fireStation);
            if (fireStationToProcess != null) {
                fireStationToProcess = fireStationRepository.updateFireStation(fireStation);
                log.info(fireStation.getAddress() + " " + fireStation.getStation() + " updated !");
            }
            else
                log.error("FireStation update failed :: " + fireStation + "does not exist");
        }
        else{
            fireStationToProcess = null;
            log.error("FireStation update failed :: Can't update a null fire station");
        }
        return fireStationToProcess;
    }

    /**
     * Delete a given fire station
     * Log error if given fire station doesn't exists in data list or is null
     * @param fireStation to delete
     * @return true if suppression is ok
     */
    @Override
    public boolean deleteFireStation(FireStation fireStation) {
        boolean deleteResult = false;
        if (fireStation != null){
            fireStationToProcess = getFireStationIfExists(fireStation);
            if (fireStationToProcess != null) {
                fireStationRepository.deleteFireStation(fireStation);
                deleteResult = true;
                log.info(fireStation.getAddress() + " " + fireStation.getStation() + " deleted from fire stations !");
            }
            else
                log.error("FireStation delete failed :: " + fireStation + "does not exist");
        }
        else
            log.error("FireStation delete failed :: Can't delete a null fire station");
        return deleteResult;
    }

    /**
     * Verify if a given fire station exists in the data list with exact match on bot address and station number
     * @param fireStation to search
     * @return fire station found if there is a match or null if no match
     */
    @Override
    public FireStation getFireStationIfExists(FireStation fireStation) {
        FireStation existingFireStation;
        List<FireStation> fireStationList = getFireStations();
        existingFireStation = fireStationList.stream()
                .filter((fs)->fs.getAddress().equalsIgnoreCase(fireStation.getAddress())&&fs.getStation()==fireStation.getStation())
                .findAny()
                .orElse(null);
        log.debug("Verify if the fire station : - " + fireStation + " - already exists");
        return existingFireStation;
    }
}
