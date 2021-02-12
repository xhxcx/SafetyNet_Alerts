package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger log = LogManager.getLogger(PersonService.class);

    private static Person personToProcess = new Person();

    @Autowired
    private PersonRepository personRepository;

    /**
     * Get all the persons from the data source
     *
     * @return a list of person
     */
    @Override
    public List<Person> getAllPersons(){
        return personRepository.getPersons();
    }

    /**
     * Save a new person
     * If person already exists return null and log error to prevent duplication
     *
     * @param person to be saved
     * @return the person saved or null if the param person is already in the list
     */
    @Override
    public Person savePerson(Person person){
        if(null != person) {
            personToProcess = getPersonIfExistsInDataList(person.getFirstName(), person.getLastName());
            if (null == personToProcess) {
                //TODO remove condition is empty on firstName and lastName that duplicate getPersonIfExist
                if (!person.getFirstName().isEmpty() && !person.getLastName().isEmpty()) {
                    personToProcess = personRepository.createPerson(person);
                }
            }
            else {
                log.error("Creation failed :: The person : " + person + " is already known");
                personToProcess=null;
            }
        }
        else {
            log.error("Creation failed :: can not create a null person");
            personToProcess = null;
        }

        return personToProcess;
    }

    /**
     * Modify an existing person
     * If the person is not found in the list of person, return null and log an error
     *
     * @param person to modify
     * @return the modified person or null if the param person doesn't exist
     */
    @Override
    public Person modifyPerson (Person person){
        if(null != person) {
            personToProcess = getPersonIfExistsInDataList(person.getFirstName(), person.getLastName());
            if (null != personToProcess) {
                personToProcess = personRepository.updatePerson(person);
            } else
                log.error("Modification failed :: No person match for first name = " + person.getFirstName() + " - last name = " + person.getLastName());
        }
        else {
            log.error("Modification failed :: can not modify a null person");
            personToProcess = null;
        }

        return personToProcess;

    }

    /**
     * Delete an existing person based on the firstName and lastName as identifier
     * If no match, log an error
     *
     * @param firstName
     * @param lastName
     *
     * @Return true if suppression is ok
     */
    @Override
    public boolean deletePerson(String firstName, String lastName){
        boolean isDeleted = false;
        if(null != firstName && null != lastName) {
            personToProcess = getPersonIfExistsInDataList(firstName, lastName);

            if (null != personToProcess) {
                personRepository.deletePerson(personToProcess);
                isDeleted = true;
            } else
                log.error("Suppression failed :: No person match for first name = " + firstName + " - last name = " + lastName);
        }
        else
            log.error("Suppression failed :: null parameter first name and/or last name");
        return isDeleted;
    }

    /**
     * Method to verify if the person for identifier first name and last name already exists in the list.
     * If params are null, log an error and return false
     *
     * @param firstName of the person to verify
     * @param lastName of the person to verify
     * @return the existing person if exists, else return null
     */
    @Override
    public Person getPersonIfExistsInDataList(String firstName, String lastName){
        Person existingPerson = new Person();
        //TODO eviter de refaire appel à get ?
        List<Person> allPersons = getAllPersons();
        if (!firstName.isEmpty() && !lastName.isEmpty() && null != firstName && null != lastName) {
            existingPerson = allPersons.stream()
                .filter((p) -> firstName.equalsIgnoreCase(p.getFirstName()) && lastName.equalsIgnoreCase(p.getLastName()))
                .findAny()
                .orElse(null);
        }
        else
            log.error("Operation failed :: can not operate a person with empty or null first name / last name");

        return existingPerson;
    }

    /**
     * Get all emails from persons living in a specific city
     *
     * @param cityName where we need to get emails
     * @return a list of emails if some persons live in the city and have an email, otherwise return an empty list
     */
    @Override
    public List<String> getPersonsEmailsByCity(String cityName) {
        if (null != getAllPersons() && null != cityName && !cityName.isEmpty()) {
            List<Person> personListInCity = getAllPersons().stream()
                    .filter(p -> p.getCity().equalsIgnoreCase(cityName))
                    .collect(Collectors.toList());

            if(!personListInCity.isEmpty()){
                return personListInCity.stream()
                        .filter(person -> (null != person.getEmail() && !person.getEmail().isEmpty()))
                        .map(person -> person.getEmail())
                        .collect(Collectors.toList());
            }
            else {
                log.info("No email identified for city : " + cityName);
                return new ArrayList<>();
            }
        }
        else {
            log.error("Get emails for city failed :: can't search for a null or empty city");
            return new ArrayList<>();
        }
    }

}
