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


@Repository
public class PersonRepository {

    private static final Logger log = LogManager.getLogger(PersonRepository.class);

    @Autowired
    private AlertsData getAlertsData;

    @Autowired
    private AlertsDataOutputWriter alertsDataOutputWriter;

    @Value("${outputFilePath}")
    private String outputFilePath;

    public List<Person> getPersons() {
        log.info("Getting all persons from the file !");
        return getAlertsData.getPersons();
    }

    public Person createPerson(Person person){
        log.debug("Try to create new person");
        getAlertsData.getPersons().add(person);
        log.info("New person created with following informations :" + person);
        //TODO eviter de devoir écrire à chaque appel
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        return person;
    }

    public Person updatePerson(Person person){
        log.debug("Try to update person named : " + person.getFirstName() + " " + person.getLastName());
        Person personToModify = getAlertsData.getPersons().stream()
                .filter((p)->p.getFirstName().equalsIgnoreCase(person.getFirstName()) && p.getLastName().equalsIgnoreCase(person.getLastName()))
                .findAny()
                .orElse(null);
        if (null != personToModify){
            int personToUpdateId = getAlertsData.getPersons().indexOf(personToModify);
            getAlertsData.getPersons().set(personToUpdateId,person);
            alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
            log.info(person.getFirstName() + " " + person.getLastName() + " updated !");
        }
        return person;
    }

    public void deletePerson(Person person){
        log.debug("Try to delete person with named : " + person.getFirstName() + " " + person.getLastName());
        getAlertsData.getPersons().remove(person);
        alertsDataOutputWriter.writeAsJsonIntoFile(getAlertsData,outputFilePath);
        log.info(person.getFirstName() + " " + person.getLastName() + " deleted from persons !");
    }

    public List<Person> getPersonsByAddress(String address){
       return getAlertsData.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }
}
