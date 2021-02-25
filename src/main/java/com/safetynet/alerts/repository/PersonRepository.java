package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.utils.AlertsDataOutputWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Implementation of CRUD methods for Person object
 *
 */
@Repository
public class PersonRepository {

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<Person> getPersons() {
        return getAlertsData.getPersons();
    }

    public Person createPerson(Person person){
        getAlertsData.getPersons().add(person);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        return person;
    }

    public Person updatePerson(Person person){
        Person personToModify = getAlertsData.getPersons().stream()
                .filter((p)->p.getFirstName().equalsIgnoreCase(person.getFirstName()) && p.getLastName().equalsIgnoreCase(person.getLastName()))
                .findAny()
                .orElse(null);
        if (null != personToModify){
            int personToUpdateId = getAlertsData.getPersons().indexOf(personToModify);
            getAlertsData.getPersons().set(personToUpdateId,person);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        }
        return person;
    }

    public void deletePerson(Person person){
        getAlertsData.getPersons().remove(person);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
    }

    public List<Person> getPersonsByAddress(String address){
       return getAlertsData.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }
}
