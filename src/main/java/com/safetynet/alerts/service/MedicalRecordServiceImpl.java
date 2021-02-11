package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger log = LogManager.getLogger(MedicalRecordService.class);

    private static MedicalRecord medicalRecordToProcess = null;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordRepository.getMedicalRecords();
    }

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        if(null != medicalRecord){
            medicalRecordToProcess = getMedicalRecordIfExists(medicalRecord.getFirstName(),medicalRecord.getLastName());
            if (null == medicalRecordToProcess){
               medicalRecordToProcess = medicalRecordRepository.createMedicalRecord(medicalRecord);
            }
            else {
                medicalRecordToProcess=null;
                log.error("MedicalRecord creation failed :: MedicalRecord \"" + medicalRecord + "\" already exists");
            }
        }
        else{
            medicalRecordToProcess=null;
            log.error("MedicalRecord creation failed :: Can't create a null medical record");
        }

        return medicalRecordToProcess;
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        if(null != medicalRecord){
            medicalRecordToProcess = getMedicalRecordIfExists(medicalRecord.getFirstName(),medicalRecord.getLastName());
            if (null != medicalRecordToProcess)
                medicalRecordToProcess = medicalRecordRepository.updateMedicalRecord(medicalRecord);
            else
                log.error("MedicalRecord update failed :: " + medicalRecord + "does not exist");
        }
        else{
            //TODO pourquoi besoin de le préciser alors que dans la décla de la variable j'ai null et que ce n'est pas set entre temps ?
            medicalRecordToProcess = null;
            log.error("MedicalRecord update failed :: Can't update a null medical record");
        }
        return medicalRecordToProcess;
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        boolean deleteResult = false;
        if (null != firstName && null != lastName){
            medicalRecordToProcess = getMedicalRecordIfExists(firstName,lastName);
            if (null != medicalRecordToProcess) {
                medicalRecordRepository.deleteMedicalRecord(medicalRecordToProcess);
                deleteResult = true;
            }
            else
                log.error("Suppression failed :: No medical record match for first name = " + firstName + " - last name = " + lastName);
        }
        else
            log.error("MedicalRecord delete failed :: Can't delete a null medical record");
        return deleteResult;
    }

    @Override
    public MedicalRecord getMedicalRecordIfExists(String firstName, String lastName) {
        MedicalRecord existingMedicalRecord = new MedicalRecord();
        //TODO eviter de refaire appel à get ?
        List<MedicalRecord> allMedicalRecords = getMedicalRecords();
        if (!firstName.isEmpty() && !lastName.isEmpty() && null != firstName && null != lastName) {
            existingMedicalRecord = allMedicalRecords.stream()
                    .filter((p) -> firstName.equalsIgnoreCase(p.getFirstName()) && lastName.equalsIgnoreCase(p.getLastName()))
                    .findAny()
                    .orElse(null);
        }
        else
            log.error("Operation failed :: can not operate a person with empty or null first name / last name");

        return existingMedicalRecord;
    }
}
