package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private static List<FireStation> fireStationList;
    private static List<Person> personList;

    @Mock
    private static PersonRepository personRepositoryMock;

    @Mock
    private static FireStationRepository fireStationRepositoryMock;

    @InjectMocks
    private FireStationDistrictServiceImpl fireStationDistrictService;

    @BeforeEach
    private void setUpPerTest(){
        FireStation fireStation = new FireStation();
        fireStation.setAddress("1 rue de paris");
        fireStation.setStation(1);

        Person person1 = new Person();
        person1.setFirstName("toto");
        person1.setLastName("test");
        person1.setAddress("1 rue de paris");
        person1.setPhone("01202202020");

        Person person2 = new Person();
        person2.setFirstName("tyler");
        person2.setLastName("durden");
        person2.setAddress("1 rue de paris");
        person2.setPhone("0199999999");

        fireStationList = new ArrayList<>();
        personList = new ArrayList<>();

        fireStationList.add(fireStation);
        personList.add(person1);
        personList.add(person2);

    }

    @Test
    public void getPhonesByFireStationNumberTest(){
        List<String> expectedPhoneList = new ArrayList<>();
        expectedPhoneList.add("01202202020");
        expectedPhoneList.add("0199999999");

        when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
        when(personRepositoryMock.getPersons()).thenReturn(personList);
        assertThat(fireStationDistrictService.getPhonesByStationNumber(1)).isEqualTo(expectedPhoneList);

    }

    @Test
    public void getPhonesByFireStationNumberReturnEmptyListTest(){

        when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
        when(personRepositoryMock.getPersons()).thenReturn(personList);
        assertThat(fireStationDistrictService.getPhonesByStationNumber(0)).isEqualTo(new ArrayList<>());

    }

}
