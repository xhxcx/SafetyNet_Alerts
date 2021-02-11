package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.utils.AlertsDataOutputWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository {

    private static final Logger log = LogManager.getLogger(MedicalRecordRepository.class);

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<MedicalRecord> getMedicalRecords() { return getAlertsData.getMedicalrecords(); }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Try to create new medical record");
        getAlertsData.getMedicalrecords().add(medicalRecord);
        log.info("New medical record created with following informations :" + medicalRecord);
        //TODO eviter de devoir écrire à chaque appel
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Try to update medical record for : " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
        MedicalRecord medicalRecordToModify = getAlertsData.getMedicalrecords().stream()
                .filter((medRecord)->medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) && medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName()))
                .findAny()
                .orElse(null);
        if (null != medicalRecordToModify){
            getAlertsData.getMedicalrecords().set(getAlertsData.getMedicalrecords().indexOf(medicalRecordToModify),medicalRecord);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
            log.info("Medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " updated !");
        }
        return medicalRecord;
    }

    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Try to delete medical record for : " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
        getAlertsData.getMedicalrecords().remove(medicalRecord);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        log.info("Medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " deleted from persons !");
    }
}
