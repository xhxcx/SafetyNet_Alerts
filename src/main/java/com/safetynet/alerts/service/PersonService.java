package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonService {

    List<Person> getAllPersons();

    Person savePerson(Person person);

    Person modifyPerson (Person person);

    boolean deletePerson(String firstName, String lastName);

    Person getPersonIfExistsInDataList(String firstName, String lastName);

    List<String> getPersonsEmailsByCity(String cityName);

}
