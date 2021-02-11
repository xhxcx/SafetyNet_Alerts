package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonServiceImpl;
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
public class PersonServiceIT {

    @Autowired
    private PersonServiceImpl personServiceUT;

    @Value("${outputFilePath}")
    private String outputFilePath;

    @BeforeEach
    private void setUpPerTest(){
        new File(outputFilePath).delete();
    }

    @Test
    @Order(1)
    public void personService_shouldReturnAPersonList_whenGetAllPersons(){
        List<Person> personList = personServiceUT.getAllPersons();
        assertThat(personList.size()).isEqualTo(23);
    }

    @Test
    @Order(4)
    public void personService_shouldReturnTheCreatedPerson_andAddTheNewPersonInOutputJsonFile_whenSavePerson(){
       Person personToCreate = new Person();
       personToCreate.setFirstName("New");
       personToCreate.setLastName("Person");
       Person resultPerson = personServiceUT.savePerson(personToCreate);

       assertThat(resultPerson.getFirstName()).isEqualTo("New");
       assertThat(resultPerson.getLastName()).isEqualTo("Person");
       assertThat(personServiceUT.getAllPersons().contains(personToCreate)).isTrue();
       assertThat(new File(outputFilePath)).exists();
    }

    @Test
    @Order(2)
    public void personService_shouldReturnTheUpdatedPerson_andUpdateThePersonInOutputJsonFile_whenModifyPerson(){
        Person personToModify = new Person();
        personToModify.setFirstName("Eric");
        personToModify.setLastName("Cadigan");
        Person resultPerson = personServiceUT.modifyPerson(personToModify);

        assertThat(resultPerson.getFirstName()).isEqualTo("Eric");
        assertThat(resultPerson.getLastName()).isEqualTo("Cadigan");
        assertThat(personServiceUT.getAllPersons().get(22).getAddress()).isEqualTo(null);
        assertThat(new File(outputFilePath)).exists();
    }

    @Test
    @Order(3)
    public void personService_shouldReturnTrue_andDeleteThePersonInOutputJsonFile_whenDeletePerson(){
        assertThat(personServiceUT.deletePerson("Eric","Cadigan")).isTrue();
        assertThat(personServiceUT.getAllPersons().size()).isEqualTo(22);
        assertThat(new File(outputFilePath)).exists();
    }

}
