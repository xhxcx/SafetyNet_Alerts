package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    @PostMapping("/person")
    public void createNewPerson(@RequestBody Person personToCreate){
        personService.savePerson(personToCreate);
    }

    @PutMapping("/person")
    public void updatePerson(@RequestBody Person personToUpdate){
        personService.modifyPerson(personToUpdate);
    }

    @DeleteMapping("/person")
    public void deletePerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName){
        personService.deletePerson(firstName,lastName);
    }


}
