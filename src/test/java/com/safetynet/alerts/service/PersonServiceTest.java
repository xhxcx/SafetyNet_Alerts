package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
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
public class PersonServiceTest {

    private static Person person = new Person();
    private static List<Person> personList = new ArrayList<>();

    @Mock
    private PersonRepository personRepositoryMock;

    @InjectMocks
    private PersonServiceImpl personServiceUT;

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

    @Nested
    @DisplayName("Get persons tests")
    class GetPersonCasesTests{
        @Test
        public void getAllPersonsTest(){
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.getAllPersons()).containsOnly(person);
            verify(personRepositoryMock, Mockito.times(1)).getPersons();

        }

        @Test
        public void getAllPersonsReturnEmptyListTest(){
            when(personRepositoryMock.getPersons()).thenReturn(new ArrayList<>());
            assertThat(personServiceUT.getAllPersons()).isEmpty();
            verify(personRepositoryMock, Mockito.times(1)).getPersons();
        }

        @Test
        public void getAllPersonsReturnNullTest(){
            when(personRepositoryMock.getPersons()).thenReturn(null);
            assertThat(personServiceUT.getAllPersons()).isNull();
            verify(personRepositoryMock, Mockito.times(1)).getPersons();
        }

    }

    @Nested
    @DisplayName("Save person tests")
    class SavePersonCaseTests{
        @Test
        public void savePersonTest(){
            Person personToAdd = new Person();
            personToAdd.setFirstName("tyler");
            personToAdd.setLastName("durden");
            personToAdd.setAddress("25 rue de Nantes");
            personToAdd.setCity("Nantes");
            personToAdd.setZip("44000");
            personToAdd.setPhone("0120304051");
            personToAdd.setEmail("tyler@durden.com");

            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.createPerson(any(Person.class))).thenReturn(personToAdd);
            assertThat(personServiceUT.savePerson(personToAdd)).isEqualTo(personToAdd);
            verify(personRepositoryMock, Mockito.times(1)).createPerson(any(Person.class));
        }

        @Test
        public void saveAlreadyExistingPersonTest(){
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.createPerson(person)).thenReturn(null);
            assertThat(personServiceUT.savePerson(person)).isNull();
            verify(personRepositoryMock, Mockito.times(0)).createPerson(any(Person.class));
        }

        @Test
        public void saveANullPersonTest(){
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.savePerson(null)).isNull();
            verify(personRepositoryMock, Mockito.times(0)).createPerson(any(Person.class));
        }

        @Test
        public void savePersonWithEmptyNameTest(){
            Person personToAdd = new Person();
            personToAdd.setFirstName("");
            personToAdd.setLastName("");
            personToAdd.setAddress("25 rue de Nantes");
            personToAdd.setCity("Nantes");
            personToAdd.setZip("44000");
            personToAdd.setPhone("0120304051");
            personToAdd.setEmail("tyler@durden.com");

            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.savePerson(personToAdd)).isNull();
            verify(personRepositoryMock, Mockito.times(0)).createPerson(any(Person.class));
        }

    }

    @Nested
    @DisplayName("Modify person tests")
    class ModifyPersonCaseTests{
        @Test
        public void modifyPersonTest(){
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            when(personRepositoryMock.updatePerson(any(Person.class))).thenReturn(person);
            assertThat(personServiceUT.modifyPerson(person)).isEqualTo(person);
            verify(personRepositoryMock,Mockito.times(1)).updatePerson(person);

        }

        @Test
        public void modifyNonExistingPersonTest(){
            Person nonExistingPerson = new Person();
            nonExistingPerson.setFirstName("tyler");
            nonExistingPerson.setLastName("durden");
            nonExistingPerson.setAddress("25 rue de Nantes");
            nonExistingPerson.setCity("Nantes");
            nonExistingPerson.setZip("44000");
            nonExistingPerson.setPhone("0120304051");
            nonExistingPerson.setEmail("tyler@durden.com");

            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.modifyPerson(nonExistingPerson)).isNull();
            verify(personRepositoryMock,Mockito.times(0)).updatePerson(person);

        }

        @Test
        public void modifyNullPersonTest(){
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.modifyPerson(null)).isNull();
            verify(personRepositoryMock,Mockito.times(0)).updatePerson(person);

        }
    }

    @Nested
    @DisplayName("Delete person tests")
    class DeletePersonCaseTests {
        @Test
        public void deletePersonTest() {
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.deletePerson(person.getFirstName(), person.getLastName())).isTrue();
            verify(personRepositoryMock, Mockito.times(1)).deletePerson(person);

        }

        @Test
        public void deleteNonExistingPersonTest() {
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.deletePerson("Tyler", "Durden")).isFalse();
            verify(personRepositoryMock, Mockito.times(0)).deletePerson(person);
        }

        @Test
        public void deletePersonWithAtLeastANullParametersTest() {
            when(personRepositoryMock.getPersons()).thenReturn(personList);
            assertThat(personServiceUT.deletePerson(null, person.getLastName())).isFalse();
            verify(personRepositoryMock, Mockito.times(0)).deletePerson(person);
        }
    }

}
