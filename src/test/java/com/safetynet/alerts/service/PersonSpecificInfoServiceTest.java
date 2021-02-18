package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PersonSpecificInfoServiceTest {

    private static Person person1 = new Person();

    @Mock
    private PersonRepository personRepositoryMock;

    @Mock
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @InjectMocks
    private PersonSpecificInfoServiceImpl personSpecificInfoService;

    @BeforeEach
    private void setUpPerTest(){

        person1.setFirstName("tyler");
        person1.setLastName("durden");
        person1.setAddress("5th Av NYC");
        person1.setEmail("tyler.durden@email.com");
        person1.setPhone("01202202020");
    }

    @Nested
    @DisplayName("PersonInfo tests")
    class GetPersonInfoTests {
        @Test
        public void getPersonInfoByName() {

            List<Person> personList = new ArrayList<>();
            personList.add(person1);

            MedicalRecord record = new MedicalRecord();
            List<String> medicationList = new ArrayList<>();
            List<String> allergiesList = new ArrayList<>();

            medicationList.add("medication");
            allergiesList.add("allergie");

            record.setFirstName("tyler");
            record.setLastName("durden");
            record.setBirthdate("01/01/1980");
            record.setMedications(medicationList);
            record.setAllergies(allergiesList);

            PersonInfoDTO expectedPersonInfoDTO = new PersonInfoDTO();
            expectedPersonInfoDTO.setLastName("durden");
            expectedPersonInfoDTO.setAddress("5th Av NYC");
            expectedPersonInfoDTO.setAge(LocalDate.now().getYear() - 1980);
            expectedPersonInfoDTO.setEmail("tyler.durden@email.com");
            expectedPersonInfoDTO.setMedications(medicationList);
            expectedPersonInfoDTO.setAllergies(allergiesList);

            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(medicalRecordRepositoryMock.getMedicalRecordByName(any(String.class), any(String.class))).thenReturn(record);

            List<PersonInfoDTO> result = personSpecificInfoService.getPersonInfo("tyler", "durden");
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.contains(expectedPersonInfoDTO)).isTrue();
        }

        @Test
        public void getPersonInfoByNameForUnknown() {

            List<Person> personList = new ArrayList<>();
            personList.add(person1);

            MedicalRecord record = new MedicalRecord();
            List<String> medicationList = new ArrayList<>();
            List<String> allergiesList = new ArrayList<>();

            medicationList.add("medication");
            allergiesList.add("allergie");

            record.setFirstName("tyler");
            record.setLastName("durden");
            record.setBirthdate("01/01/1980");
            record.setMedications(medicationList);
            record.setAllergies(allergiesList);

            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(medicalRecordRepositoryMock.getMedicalRecordByName(any(String.class), any(String.class))).thenReturn(record);

            List<PersonInfoDTO> result = personSpecificInfoService.getPersonInfo("soldat", "inconnu");
            assertThat(result.size()).isEqualTo(0);

        }
    }
    @Nested
    @DisplayName("ChildAlert tests")
    class ChildAlertTests{
        @Test
        public void getChildAlertWithChild(){
            List<Person> personList = new ArrayList<>();
            personList.add(person1);
            Person father = new Person();
            father.setFirstName("Jack");
            father.setLastName("durden");
            personList.add(father);

            MedicalRecord record = new MedicalRecord();
            record.setFirstName("tyler");
            record.setLastName("durden");
            record.setBirthdate("01/01/2010");

            MedicalRecord fatherRecord = new MedicalRecord();
            fatherRecord.setFirstName("Jack");
            fatherRecord.setLastName("durden");
            fatherRecord.setBirthdate("01/01/1980");

            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepositoryMock.getMedicalRecordByName(any(String.class), any(String.class))).thenReturn(record,fatherRecord);

            List<ChildAlertDTO> result = personSpecificInfoService.getChildAlertByAddress("5th Av NYC");
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.get(0).getChildList().get(0).getFirstName()).isEqualTo("tyler");
            assertThat(result.get(0).getFamilyMemberList().get(0).getFirstName()).isEqualTo("Jack");

        }

        @Test
        public void getChildAlertWithoutChild(){
            List<Person> personList = new ArrayList<>();
            personList.add(person1);
            Person father = new Person();
            father.setFirstName("Jack");
            father.setLastName("durden");
            personList.add(father);

            MedicalRecord record = new MedicalRecord();
            record.setFirstName("tyler");
            record.setLastName("durden");
            record.setBirthdate("01/01/2000");

            MedicalRecord fatherRecord = new MedicalRecord();
            fatherRecord.setFirstName("Jack");
            fatherRecord.setLastName("durden");
            fatherRecord.setBirthdate("01/01/1980");

            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepositoryMock.getMedicalRecordByName(any(String.class), any(String.class))).thenReturn(record,fatherRecord);

            List<ChildAlertDTO> result = personSpecificInfoService.getChildAlertByAddress("5th Av NYC");
            assertThat(result.size()).isEqualTo(0);
        }

        @Test
        public void getChildAlertChildWithNobodyAtAddress(){
            List<Person> personList = new ArrayList<>();
            personList.add(person1);
            Person father = new Person();
            father.setFirstName("Jack");
            father.setLastName("durden");
            personList.add(father);

            MedicalRecord record = new MedicalRecord();
            record.setFirstName("tyler");
            record.setLastName("durden");
            record.setBirthdate("01/01/2000");

            MedicalRecord fatherRecord = new MedicalRecord();
            fatherRecord.setFirstName("Jack");
            fatherRecord.setLastName("durden");
            fatherRecord.setBirthdate("01/01/1980");

            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepositoryMock.getMedicalRecordByName(any(String.class), any(String.class))).thenReturn(record,fatherRecord);

            List<ChildAlertDTO> result = personSpecificInfoService.getChildAlertByAddress("");
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
