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

    /**
     * Get all medical records
     *
     * @return a list containing all medical records
     */
    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordRepository.getMedicalRecords();
    }

    /**
     * Save a medical record
     * Medical record should be new to be saved otherwise log error
     *
     * @param medicalRecord to save
     * @return the medical record if saved, else return null
     */
    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        if(null != medicalRecord && !medicalRecord.getFirstName().isEmpty() && !medicalRecord.getLastName().isEmpty()){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(medicalRecord.getFirstName(),medicalRecord.getLastName());
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
            log.error("MedicalRecord creation failed :: Can't create medical record for null or empty person");
        }

        return medicalRecordToProcess;
    }

    /**
     * Update a medical record
     * if medical record doesn't exists in the list an error is logged
     *
     * @param medicalRecord to update
     * @return updated medical record if update is ok, else return null
     */
    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        if(null != medicalRecord){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(medicalRecord.getFirstName(),medicalRecord.getLastName());
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

    /**
     * Delete a medical record
     * if medical record doesn't exists in the list an error is logged
     * Match on medical record made on combination first name / last name
     *
     * @param firstName first name of the owner of the medical record
     * @param lastName last name of the owner of the medical record
     * @return true if delete succeed
     */
    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        boolean deleteResult = false;
        if (null != firstName && null != lastName){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(firstName,lastName);
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
}
