package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonRepositoryTest {

    private static Person person = new Person();
    private static List<Person> personList = new ArrayList<>();

    @MockBean
    private PersonRepository personRepositoryMock;

    @BeforeEach
    private void setUpPerTest(){
        person.setFirstName("toto");
        person.setLastName("test");
        person.setAddress("25 rue de paris");
        person.setCity("Paris");
        person.setZip("75000");
        person.setPhone("0120304050");
        person.setEmail("toto@test.com");

        personList.add(person);
    }

    @Test
    public void getPersonsTest(){
        when(personRepositoryMock.getPersons()).thenReturn(personList);
    }

    @Test
    public void createPersonTest(){

    }

    @Test
    public void updatePersonTest(){

    }

    @Test
    public void deletePersonTest(){

    }

}
