package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.DisasterVictimDTO;
import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FloodDTO;
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

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    private MedicalRecord setNewMedicalRecord(String firstName, String lastName, String birthDate, String medication, String allergie){
        MedicalRecord record = new MedicalRecord();
        List<String> medicationList = new ArrayList<>();
        List<String> allergiesList = new ArrayList<>();

        medicationList.add(medication);
        allergiesList.add(allergie);

        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setBirthdate(birthDate);
        record.setMedications(medicationList);
        record.setAllergies(allergiesList);

        return record;
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

            MedicalRecord medicalRecordToto = setNewMedicalRecord("toto","test","01/01/2020","med1","al");
            MedicalRecord medicalRecordTyler = setNewMedicalRecord("tyler","durden","01/01/1980","","peanut");

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

            MedicalRecord medicalRecordToto = setNewMedicalRecord("toto","test","01/01/2020","med1","al");

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

    @Nested
    @DisplayName("Fire information tests")
    class GetFireInformationTests{
        @Test
        public void getFireInformationByAddressTest(){
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();
            List<MedicalRecord> medicalRecordList = new ArrayList<>();

            MedicalRecord medicalRecordToto = setNewMedicalRecord("toto","test","01/01/2020","med1","al");

            fireStationList.add(fireStation);
            personList.add(person1);
            medicalRecordList.add(medicalRecordToto);

            DisasterVictimDTO victimToto = new DisasterVictimDTO();
            victimToto.setLastName(person1.getLastName());
            victimToto.setPhone(person1.getPhone());
            victimToto.setAge(LocalDate.now().getYear() - 2020);
            victimToto.setMedications(medicalRecordToto.getMedications());
            victimToto.setAllergies(medicalRecordToto.getAllergies());

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepository.getMedicalRecordByName(any(String.class),any(String.class))).thenReturn(medicalRecordToto);

            FireDTO result = fireStationDistrictService.getFireInformationByAddress("1 rue de paris");
            assertThat(result.getStationNumberList().size()).isEqualTo(1);
            assertThat(result.getStationNumberList().contains(1));
            assertThat(result.getVictimList().size()).isEqualTo(1);
            assertThat(result.getVictimList().contains(victimToto)).isTrue();
        }

        @Test
        public void getFireInformationByEmptyAddressTest(){
            FireDTO result = fireStationDistrictService.getFireInformationByAddress("");
            assertThat(result.getStationNumberList()).isNull();
            assertThat(result.getVictimList()).isNull();
        }
    }

    @Nested
    @DisplayName("Flood information tests")
    class GetFloodInformationByStationsTest{
        @Test
        public void getFloodInformationByStationsTest(){
            List<FireStation> fireStationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();
            List<MedicalRecord> medicalRecordList = new ArrayList<>();

            MedicalRecord medicalRecordToto = setNewMedicalRecord("toto","test","01/01/2020","med1","al");
            MedicalRecord medicalRecordTyler = setNewMedicalRecord("tyler","durden","01/01/1980","","peanut");

            fireStationList.add(fireStation);
            personList.add(person1);
            personList.add(person2);
            medicalRecordList.add(medicalRecordToto);
            medicalRecordList.add(medicalRecordTyler);

            DisasterVictimDTO victimToto = new DisasterVictimDTO();
            victimToto.setLastName(person1.getLastName());
            victimToto.setPhone(person1.getPhone());
            victimToto.setAge(LocalDate.now().getYear() - 2020);
            victimToto.setMedications(medicalRecordToto.getMedications());
            victimToto.setAllergies(medicalRecordToto.getAllergies());

            DisasterVictimDTO victimTyler = new DisasterVictimDTO();
            victimTyler.setLastName(person2.getLastName());
            victimTyler.setPhone(person2.getPhone());
            victimTyler.setAge(LocalDate.now().getYear() - 1980);
            victimTyler.setMedications(medicalRecordTyler.getMedications());
            victimTyler.setAllergies(medicalRecordTyler.getAllergies());

            Map<String,List<DisasterVictimDTO>> expectedMap = new HashMap<>();
            expectedMap.put("1 rue de paris",new ArrayList<>(Arrays.asList(victimToto,victimTyler)));

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.getPersonsByAddress(any(String.class))).thenReturn(personList);
            when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecordList);
            when(medicalRecordRepository.getMedicalRecordByName("toto","test")).thenReturn(medicalRecordToto);
            when(medicalRecordRepository.getMedicalRecordByName("tyler","durden")).thenReturn(medicalRecordTyler);

            FloodDTO floodDTO = fireStationDistrictService.getFloodInformationByStations(new ArrayList<>(Arrays.asList(1,99)));

            assertThat(floodDTO.getFamilyByAddressList().containsKey("1 rue de paris")).isTrue();
            assertThat(floodDTO.getFamilyByAddressList()).isEqualTo(expectedMap);

        }

        @Test
        public void getFloodInformationByEmptyStationsTest(){
            assertThat(fireStationDistrictService.getFloodInformationByStations(new ArrayList<>())).isNull();

        }

        @Test
        public void getFloodInformationByNonExistingStationsTest(){
            FloodDTO floodDTO = fireStationDistrictService.getFloodInformationByStations(new ArrayList<>(Arrays.asList(99)));
            assertThat(floodDTO.getFamilyByAddressList()).isEmpty();
        }
    }

}
