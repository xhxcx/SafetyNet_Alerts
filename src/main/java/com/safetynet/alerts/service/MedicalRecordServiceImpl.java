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
        if(medicalRecord != null && !medicalRecord.getFirstName().isEmpty() && !medicalRecord.getLastName().isEmpty()){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(medicalRecord.getFirstName(),medicalRecord.getLastName());
            if (medicalRecordToProcess == null){
               medicalRecordToProcess = medicalRecordRepository.createMedicalRecord(medicalRecord);
                log.info("New medical record created with following information :" + medicalRecord);
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
        if(medicalRecord != null){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(medicalRecord.getFirstName(),medicalRecord.getLastName());
            if (medicalRecordToProcess != null) {
                medicalRecordToProcess = medicalRecordRepository.updateMedicalRecord(medicalRecord);
                log.info("Medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " updated !");
            }
            else
                log.error("MedicalRecord update failed :: " + medicalRecord + "does not exist");
        }
        else{
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
        if (firstName != null && lastName != null){
            medicalRecordToProcess = medicalRecordRepository.getMedicalRecordByName(firstName,lastName);
            if (medicalRecordToProcess != null) {
                medicalRecordRepository.deleteMedicalRecord(medicalRecordToProcess);
                deleteResult = true;
                log.info("Medical record of " + firstName + " " + lastName + " deleted !");
            }
            else
                log.error("MedicalRecord delete failed :: No medical record match for first name = " + firstName + " - last name = " + lastName);
        }
        else
            log.error("MedicalRecord delete failed :: Can't delete a null medical record");
        return deleteResult;
    }
}
