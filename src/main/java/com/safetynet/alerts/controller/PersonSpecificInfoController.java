package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonSpecificInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonSpecificInfoController {

    @Autowired
    private PersonSpecificInfoService personSpecificInfoService;

    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByName(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
        return personSpecificInfoService.getPersonInfo(firstName,lastName);
    }
}
