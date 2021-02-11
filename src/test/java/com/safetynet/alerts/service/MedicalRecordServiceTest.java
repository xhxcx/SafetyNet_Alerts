package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MedicalRecordServiceTest {
    private static MedicalRecord medicalRecord = new MedicalRecord();
    private static List<MedicalRecord> medicalRecordList = new ArrayList<>();

    @Mock
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @BeforeEach
    private void setUpPerTest(){
        ArrayList<String> medications = new ArrayList<>();
        medications.add("med1");
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("al");
        medicalRecord.setFirstName("toto");
        medicalRecord.setLastName("test");
        medicalRecord.setBirthdate("01/01/2021");
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);

        medicalRecordList.add(medicalRecord);
    }

    @Nested
    @DisplayName("Get medical records tests")
    class GetMedicalRecordCasesTests{
        @Test
        public void getMedicalRecordsTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.getMedicalRecords()).containsOnly(medicalRecord);
            verify(medicalRecordRepositoryMock, Mockito.times(1)).getMedicalRecords();

        }

        @Test
        public void getMedicalRecordsReturnEmptyListTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(new ArrayList<>());
            assertThat(medicalRecordService.getMedicalRecords()).isEmpty();
            verify(medicalRecordRepositoryMock, Mockito.times(1)).getMedicalRecords();
        }

        @Test
        public void getMedicalRecordsReturnNullTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(null);
            assertThat(medicalRecordService.getMedicalRecords()).isNull();
            verify(medicalRecordRepositoryMock, Mockito.times(1)).getMedicalRecords();
        }

    }

    @Nested
    @DisplayName("Save medical record tests")
    class SaveMedicalRecordCaseTests{
        @Test
        public void saveMedicalRecordTest(){
            ArrayList<String> newRecordMedications = new ArrayList<>();
            newRecordMedications.add("med1");
            ArrayList<String> newRecordAllergies = new ArrayList<>();
            newRecordAllergies.add("al");
            MedicalRecord newRecord = new MedicalRecord();
            newRecord.setFirstName("tyler");
            newRecord.setLastName("durden");
            newRecord.setBirthdate("01/01/2021");
            newRecord.setMedications(newRecordMedications);
            newRecord.setAllergies(newRecordAllergies);

            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            when(medicalRecordRepositoryMock.createMedicalRecord(any(MedicalRecord.class))).thenReturn(newRecord);
            assertThat(medicalRecordService.saveMedicalRecord(newRecord)).isEqualTo(newRecord);
            verify(medicalRecordRepositoryMock, Mockito.times(1)).createMedicalRecord(any(MedicalRecord.class));
        }

        @Test
        public void saveAlreadyExistingMedicalRecordTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            when(medicalRecordRepositoryMock.createMedicalRecord(medicalRecord)).thenReturn(null);
            assertThat(medicalRecordService.saveMedicalRecord(medicalRecord)).isNull();
            verify(medicalRecordRepositoryMock, Mockito.times(0)).createMedicalRecord(any(MedicalRecord.class));
        }

        @Test
        public void saveANullMedicalRecordTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.saveMedicalRecord(null)).isNull();
            verify(medicalRecordRepositoryMock, Mockito.times(0)).createMedicalRecord(any(MedicalRecord.class));
        }

        @Test
        public void saveMedicalRecordWithEmptyNameTest(){
            ArrayList<String> newRecordMedications = new ArrayList<>();
            newRecordMedications.add("med1");
            ArrayList<String> newRecordAllergies = new ArrayList<>();
            newRecordAllergies.add("al");
            MedicalRecord newRecord = new MedicalRecord();
            newRecord.setFirstName("");
            newRecord.setLastName("");
            newRecord.setBirthdate("01/01/2021");
            newRecord.setMedications(newRecordMedications);
            newRecord.setAllergies(newRecordAllergies);

            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.saveMedicalRecord(newRecord)).isNull();
            verify(medicalRecordRepositoryMock, Mockito.times(0)).createMedicalRecord(any(MedicalRecord.class));
        }

    }

    @Nested
    @DisplayName("Modify medical record tests")
    class ModifyMedicalRecordCaseTests{
        @Test
        public void modifyMedicalRecordTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            when(medicalRecordRepositoryMock.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
            assertThat(medicalRecordService.updateMedicalRecord(medicalRecord)).isEqualTo(medicalRecord);
            verify(medicalRecordRepositoryMock,Mockito.times(1)).updateMedicalRecord(any(MedicalRecord.class));

        }

        @Test
        public void modifyNonExistingMedicalRecordTest(){
            ArrayList<String> newRecordMedications = new ArrayList<>();
            newRecordMedications.add("med1");
            ArrayList<String> newRecordAllergies = new ArrayList<>();
            newRecordAllergies.add("al");
            MedicalRecord newRecord = new MedicalRecord();
            newRecord.setFirstName("tyler");
            newRecord.setLastName("durden");
            newRecord.setBirthdate("01/01/2021");
            newRecord.setMedications(newRecordMedications);
            newRecord.setAllergies(newRecordAllergies);

            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.updateMedicalRecord(newRecord)).isNull();
            verify(medicalRecordRepositoryMock,Mockito.times(0)).updateMedicalRecord(any(MedicalRecord.class));

        }

        @Test
        public void modifyNullMedicalRecordTest(){
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.updateMedicalRecord(null)).isNull();
            verify(medicalRecordRepositoryMock,Mockito.times(0)).updateMedicalRecord(any(MedicalRecord.class));

        }
    }

    @Nested
    @DisplayName("Delete medical record tests")
    class DeleteMedicalRecordCaseTests {
        @Test
        public void deleteMedicalRecordTest() {
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName())).isTrue();
            verify(medicalRecordRepositoryMock, Mockito.times(1)).deleteMedicalRecord(any(MedicalRecord.class));

        }

        @Test
        public void deleteNonExistingMedicalRecordTest() {
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.deleteMedicalRecord("Tyler", "Durden")).isFalse();
            verify(medicalRecordRepositoryMock, Mockito.times(0)).deleteMedicalRecord(any(MedicalRecord.class));
        }

        @Test
        public void deleteMedicalRecordWithAtLeastANullParametersTest() {
            when(medicalRecordRepositoryMock.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(medicalRecordService.deleteMedicalRecord(null, medicalRecord.getLastName())).isFalse();
            verify(medicalRecordRepositoryMock, Mockito.times(0)).deleteMedicalRecord(any(MedicalRecord.class));
        }
    }
}
