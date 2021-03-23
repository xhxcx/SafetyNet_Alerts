package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    /**
     * Endpoint to get the list of all FireStations
     *
     * @return the whole list of FireStations
     */
    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations(){ return fireStationService.getFireStations(); }

    /**
     * Endpoint to create a new fire station
     *
     * @param fireStation as JSON in the body of the request
     *
     * @return a 201 ResponseEntity with fire station or throws a ResponseStatusException with BAD_REQUEST
     */
    @PostMapping("/firestation")
    public ResponseEntity<?> addNewFireStation(@RequestBody FireStation fireStation){
        FireStation fireStationCreated = fireStationService.saveFireStation(fireStation);
        if (fireStationCreated == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid argument to create a FireStation");
        }
        else
            return new ResponseEntity<>(fireStation, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update a given fire station
     * @param fireStation as JSON in the body of the request
     * @return A 200 ResponseEntity with the updated fire station as JSON or throws a ResponseStatusException with NOT_FOUND
     */
    //TODO Pb quand plusieurs stations avec meme addresse mais numéro différent => ajout de param ?
    @PutMapping("/firestation")
    public ResponseEntity<?> modifyFireStation(@RequestBody FireStation fireStation ){
        FireStation fireStationUpdated = fireStationService.updateFireStation(fireStation);
        if (fireStationUpdated == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FireStation doesn't exist");
        }
        else
            return new ResponseEntity<>(fireStation, HttpStatus.OK);
    }

    /**
     * Endpoint to delete the given fire station
     * @param fireStation as JSON in the body of the request
     * @return A 200 ResponseEntity or throws a ResponseStatusException with NOT_FOUND
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<?> deleteFireStation(@RequestBody FireStation fireStation){
        Boolean deleteStatus = fireStationService.deleteFireStation(fireStation);
        if (!deleteStatus){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FireStation doesn't exist");
        }
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
