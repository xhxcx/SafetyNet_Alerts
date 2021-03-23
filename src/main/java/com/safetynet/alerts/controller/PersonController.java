package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Endpoint to get the list of all persons
     *
     * @return the whole list of persons
     */
    @GetMapping("/persons")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    /**
     * Endpoint to create a new person
     *
     * @param personToCreate as JSON in the body of the request
     *
     * @return a 201 ResponseEntity with person or throws a ResponseStatusException with BAD_REQUEST
     */
    @PostMapping("/person")
    public ResponseEntity<?> createNewPerson(@RequestBody Person personToCreate){
        Person personResponse = personService.savePerson(personToCreate);
        if (personResponse == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument to create a person");
        }
        else
            return new ResponseEntity<>(personToCreate, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update a given person based on the combination of first name and last name
     *
     * @param personToUpdate as JSON in the body of the request
     *
     * @return A 200 ResponseEntity with the updated person as JSON or throws a ResponseStatusException with NOT_FOUND
     */
    @PutMapping("/person")
    public ResponseEntity<?> updatePerson(@RequestBody Person personToUpdate){
        Person personResponse = personService.modifyPerson(personToUpdate);
        if (personResponse == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person doesn't exist !");
        }
        else
            return new ResponseEntity<>(personToUpdate, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a person based on the combination of first name and last name
     *
     * @param firstName as 1st parameter
     * @param lastName as 2nd parameter
     *
     * @return A 200 ResponseEntity or throws a ResponseStatusException with NOT_FOUND
     */
    @DeleteMapping("/person")
    public ResponseEntity<?> deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName){
        boolean response = personService.deletePerson(firstName,lastName);
        if (!response){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person doesn't exist !");
        }
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to get all emails from inhabitants in the given city
     *
     * @param cityName as request parameter
     *
     * @return a list of emails as string
     */
    @GetMapping("/communityEmail")
    public List<String> getPersonsEmailsByCity(@RequestParam("city") String cityName) {
        return personService.getPersonsEmailsByCity(cityName);
        //TODO normalement un response entity mais dans un temps 1 juste return l'objet demandé
    }

}
