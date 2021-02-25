package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.utils.AlertsDataOutputWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository {

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<MedicalRecord> getMedicalRecords() { return getAlertsData.getMedicalrecords(); }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        getAlertsData.getMedicalrecords().add(medicalRecord);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord medicalRecordToModify = getAlertsData.getMedicalrecords().stream()
                .filter((medRecord)->medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) && medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName()))
                .findAny()
                .orElse(null);
        if (null != medicalRecordToModify){
            getAlertsData.getMedicalrecords().set(getAlertsData.getMedicalrecords().indexOf(medicalRecordToModify),medicalRecord);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        }
        return medicalRecord;
    }

    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        getAlertsData.getMedicalrecords().remove(medicalRecord);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
    }

    public MedicalRecord getMedicalRecordByName(String firstName, String lastName){
        return getAlertsData.getMedicalrecords().stream()
                .filter(person -> firstName.equalsIgnoreCase(person.getFirstName()) && lastName.equalsIgnoreCase(person.getLastName()))
                .findAny()
                .orElse(null);
    }
}
