package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordService.getMedicalRecords();
    }

    @PostMapping("/medicalRecord")
    public void createNewPerson(@RequestBody MedicalRecord medicalRecordToCreate){ medicalRecordService.saveMedicalRecord(medicalRecordToCreate); }

    @PutMapping("/medicalRecord")
    public void updatePerson(@RequestBody MedicalRecord medicalRecordToUpdate){ medicalRecordService.updateMedicalRecord(medicalRecordToUpdate); }

    @DeleteMapping("/medicalRecord")
    public void deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName){
        medicalRecordService.deleteMedicalRecord(firstName,lastName);
    }
}
