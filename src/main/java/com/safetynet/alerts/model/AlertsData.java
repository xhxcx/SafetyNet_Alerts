package com.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

@Data
public class AlertsData {
    List<Person> persons;

    List<FireStation> firestations;

    List<MedicalRecord> medicalrecords;
}
