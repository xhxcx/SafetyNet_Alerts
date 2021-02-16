package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationDistrictServiceTest {

    private static FireStation fireStation = new FireStation();

    private static Person person1 = new Person();

    private static Person person2 = new Person();

    @Mock
    private static PersonRepository personRepositoryMock;

    @Mock
    private static FireStationRepository fireStationRepositoryMock;

    @Mock
    private static MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private FireStationDistrictServiceImpl fireStationDistrictService;

    @BeforeEach
    private void setUpPerTest(){

        fireStation.setAddress("1 rue de paris");
        fireStation.setStation(1);

        person1.setFirstName("toto");
        person1.setLastName("test");
        person1.setAddress("1 rue de paris");
        person1.setPhone("01202202020");

        person2.setFirstName("tyler");
        person2.setLastName("durden");
        person2.setAddress("1 rue de paris");
        person2.setPhone("0199999999");
    }

    @Nested
    @DisplayName("Phone Alert tests")
    class GetPhoneListTests {
        @Test
        public void getPhonesByFireStationNumberTest() {
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();

            fireStationList.add(fireStation);
            personList.add(person1);
            personList.add(person2);
            List<String> expectedPhoneList = new ArrayList<>();
            expectedPhoneList.add("01202202020");
            expectedPhoneList.add("0199999999");

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            assertThat(fireStationDistrictService.getPhonesByStationNumber(1)).isEqualTo(expectedPhoneList);

        }

        @Test
        public void getPhonesByFireStationNumberReturnEmptyListTest() {
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();

            fireStationList.add(fireStation);
            personList.add(person1);
            personList.add(person2);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(fireStationDistrictService.getPhonesByStationNumber(0)).isEqualTo(new ArrayList<>());

        }
    }

    @Nested
    @DisplayName("Fire station district coverage tests")
    class GetFireStationDistrictCoverageTests {
        @Test
        public void getFireStationDistrictCoverageTest() {
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();
            List<MedicalRecord> medicalRecordList = new ArrayList<>();

            MedicalRecord medicalRecordToto = new MedicalRecord();
            ArrayList<String> medications = new ArrayList<>();
            medications.add("med1");
            ArrayList<String> allergies = new ArrayList<>();
            allergies.add("al");
            medicalRecordToto.setFirstName("toto");
            medicalRecordToto.setLastName("test");
            medicalRecordToto.setBirthdate("01/01/2020");
            medicalRecordToto.setMedications(medications);
            medicalRecordToto.setAllergies(allergies);

            MedicalRecord medicalRecordTyler = new MedicalRecord();
            ArrayList<String> medicationsTyler = new ArrayList<>();
            ArrayList<String> allergiesTyler = new ArrayList<>();
            allergies.add("peanut");
            medicalRecordTyler.setFirstName("tyler");
            medicalRecordTyler.setLastName("durden");
            medicalRecordTyler.setBirthdate("01/01/1980");
            medicalRecordTyler.setMedications(medicationsTyler);
            medicalRecordTyler.setAllergies(allergiesTyler);

            fireStationList.add(fireStation);
            personList.add(person1);
            personList.add(person2);
            medicalRecordList.add(medicalRecordToto);
            medicalRecordList.add(medicalRecordTyler);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecordList);
            when(medicalRecordRepository.getMedicalRecordByName("toto","test")).thenReturn(medicalRecordToto);
            when(medicalRecordRepository.getMedicalRecordByName("tyler","durden")).thenReturn(medicalRecordTyler);
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(1).getAdultCount()).isEqualTo(1);
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(1).getChildrenCount()).isEqualTo(1);
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(1).getCoveredPersonDTOList().size()).isEqualTo(2);

        }

        @Test
        public void getFireStationDistrictCoverageForNonExistingFireStationTest() {
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();
            List<MedicalRecord> medicalRecordList = new ArrayList<>();

            MedicalRecord medicalRecordToto = new MedicalRecord();
            ArrayList<String> medications = new ArrayList<>();
            medications.add("med1");
            ArrayList<String> allergies = new ArrayList<>();
            allergies.add("al");
            medicalRecordToto.setFirstName("toto");
            medicalRecordToto.setLastName("test");
            medicalRecordToto.setBirthdate("01/01/2020");
            medicalRecordToto.setMedications(medications);
            medicalRecordToto.setAllergies(allergies);


            fireStationList.add(fireStation);
            personList.add(person1);
            medicalRecordList.add(medicalRecordToto);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecordList);
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(0).getCoveredPersonDTOList()).isNull();
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(0).getAdultCount()).isEqualTo(0);
            assertThat(fireStationDistrictService.getFireStationDistrictCoverage(0).getChildrenCount()).isEqualTo(0);
        }
    }

}
