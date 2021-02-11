package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MedicalRecordServiceIT {

    @Autowired
    private MedicalRecordServiceImpl medicalRecordServiceUT;

    @Value("${outputFilePath}")
    private String outputFilePath;

    @BeforeEach
    private void setUpPerTest(){
        new File(outputFilePath).delete();
    }

    @Test
    @Order(1)
    public void medicalRecordService_shouldReturnAMedicalRecordList_whenGetMedicalRecords(){
        List<MedicalRecord> medicalRecordList = medicalRecordServiceUT.getMedicalRecords();
        assertThat(medicalRecordList.size()).isEqualTo(23);
    }

    @Test
    @Order(4)
    public void medicalRecordService_shouldReturnTheCreatedMedicalRecord_andAddTheNewMedicalRecordInOutputJsonFile_whenSaveMedicalRecord(){
        MedicalRecord recordToCreate = new MedicalRecord();
        recordToCreate.setFirstName("New");
        recordToCreate.setLastName("Person");
        MedicalRecord resultRecord = medicalRecordServiceUT.saveMedicalRecord(recordToCreate);

        assertThat(resultRecord.getFirstName()).isEqualTo("New");
        assertThat(resultRecord.getLastName()).isEqualTo("Person");
        assertThat(medicalRecordServiceUT.getMedicalRecords().contains(resultRecord)).isTrue();
        assertThat(new File(outputFilePath)).exists();
    }

    @Test
    @Order(2)
    public void medicalRecordService_shouldReturnTheUpdatedMedicalRecord_andUpdateTheMedicalRecordInOutputJsonFile_whenUpdateMedicalRecord(){
        MedicalRecord recordToModify = new MedicalRecord();
        recordToModify.setFirstName("Eric");
        recordToModify.setLastName("Cadigan");
        MedicalRecord resultRecord = medicalRecordServiceUT.updateMedicalRecord(recordToModify);

        assertThat(resultRecord.getFirstName()).isEqualTo("Eric");
        assertThat(resultRecord.getLastName()).isEqualTo("Cadigan");
        assertThat(medicalRecordServiceUT.getMedicalRecords().get(22).getBirthdate()).isEqualTo(null);
        assertThat(new File(outputFilePath)).exists();
    }

    @Test
    @Order(3)
    public void medicalRecordService_shouldReturnTrue_andDeleteTheMedicalRecordInOutputJsonFile_whenDeleteMedicalRecord(){
        assertThat(medicalRecordServiceUT.deleteMedicalRecord("Eric","Cadigan")).isTrue();
        assertThat(medicalRecordServiceUT.getMedicalRecords().size()).isEqualTo(22);
        assertThat(new File(outputFilePath)).exists();
    }
}
