package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicalRecordService {

    List<MedicalRecord> getMedicalRecords();

    MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

    boolean deleteMedicalRecord(String medicalRecordFirstName, String medicalRecordLastName);
}
