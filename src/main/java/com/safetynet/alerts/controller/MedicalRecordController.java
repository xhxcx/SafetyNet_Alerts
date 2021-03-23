package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    /**
     * Endpoint to get the list of all MedicalRecords
     *
     * @return the whole list of MedicalRecords
     *
     */
    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordService.getMedicalRecords();
    }

    /**
     * Endpoint to create a new medical record
     *
     * @param medicalRecordToCreate as JSON in the body of the request
     *
     * @return a 201 ResponseEntity with medical record or throws a ResponseStatusException with BAD_REQUEST
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<?> createMedicalRecord(@RequestBody MedicalRecord medicalRecordToCreate){
        MedicalRecord medicalRecordCreated = medicalRecordService.saveMedicalRecord(medicalRecordToCreate);
        if (medicalRecordCreated == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid argument to create a medical record");
        }
        else
            return new ResponseEntity<>(medicalRecordToCreate, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update a given medical record based on the combination of first name and last name
     * @param medicalRecordToUpdate as JSON in the body of the request
     * @return A 200 ResponseEntity with the updated medical record as JSON or throws a ResponseStatusException with NOT_FOUND
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<?> updateMedicalRecord(@RequestBody MedicalRecord medicalRecordToUpdate){
        MedicalRecord medicalRecordUpdated = medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);
        if (medicalRecordUpdated == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical record doesn't exist for this person");
        }
        else
            return new ResponseEntity<>(medicalRecordToUpdate, HttpStatus.OK);

    }

    /**
     * Endpoint to delete a medical record based on the combination of first name and last name
     * @param firstName as 1st parameter
     * @param lastName as 2nd parameter
     * @return A 200 ResponseEntity or throws a ResponseStatusException with NOT_FOUND
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<?> deleteMedicalRecord(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName){
        Boolean deleteStatus = medicalRecordService.deleteMedicalRecord(firstName,lastName);
        if (!deleteStatus){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical record doesn't exist for this person");
        }
        else
            return new ResponseEntity<>(HttpStatus.OK);

    }
}
