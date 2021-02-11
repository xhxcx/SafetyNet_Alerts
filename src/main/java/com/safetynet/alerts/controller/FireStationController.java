package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations(){ return fireStationService.getFireStations(); }

    @PostMapping("/firestation")
    public FireStation addNewFireStation(@RequestBody FireStation fireStation){ return fireStationService.saveFireStation(fireStation); }

    //TODO Pb quand plusieurs stations avec meme addresse mais numéro différent => ajout de param ?
    @PutMapping("/firestation")
    public FireStation modifyFireStation(@RequestBody FireStation fireStation ){ return fireStationService.updateFireStation(fireStation); }

    @DeleteMapping("/firestation")
    public void deleteFireStation(@RequestBody FireStation fireStation){ fireStationService.deleteFireStation(fireStation);}
}
