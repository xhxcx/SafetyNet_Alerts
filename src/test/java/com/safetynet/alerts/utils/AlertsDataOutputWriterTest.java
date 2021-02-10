package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AlertsDataOutputWriterTest {

    AlertsDataOutputWriterImpl alertsDataOutputWriterUT = new AlertsDataOutputWriterImpl();

    private String testFilePath = "./src/test/resources/new_data.json";

    @BeforeEach
    private void setUpPerTest(){
        File fileToDelete = new File(testFilePath);
        fileToDelete.delete();
    }

    @Test
    public void writeAsJsonIntoFileTest(){
        Person person = new Person();
        FireStation fireStation = new FireStation();
        MedicalRecord medicalRecord = new MedicalRecord();
        List<Person> personList = new ArrayList<>();
        List<FireStation> fireStationList = new ArrayList<>();
        List<MedicalRecord> medicalRecordList =new ArrayList<>();

        personList.add(person);
        fireStationList.add(fireStation);
        medicalRecordList.add(medicalRecord);

        AlertsData alertsData = new AlertsData();
        alertsData.setPersons(personList);
        alertsData.setFirestations(fireStationList);
        alertsData.setMedicalrecords(medicalRecordList);

        alertsDataOutputWriterUT.writeAsJsonIntoFile(alertsData, testFilePath);

        assertThat(new File(testFilePath)).exists();
    }

    @Test
    public void writeAsJsonIntoFileWithNullDataTest(){
        alertsDataOutputWriterUT.writeAsJsonIntoFile(null, testFilePath);
        assertThat(new File(testFilePath)).doesNotExist();
    }

}
