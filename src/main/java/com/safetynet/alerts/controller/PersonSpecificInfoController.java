package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.dto.ChildAlertDTO;
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

    /**
     * Endpoint to get person informations based on the first name and last name
     *
     * @param firstName
     * @param lastName
     * @return A list of PersonInfoDTO, a PersonInfoDTO contains consolidated information for the given person
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByName(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
        return personSpecificInfoService.getPersonInfo(firstName,lastName);
    }

    /**
     * Endpoint to get all child living at the given address
     * Also return all family members for each children
     *
     * @param address
     * @return a list of ChildAlertDTO that contains a list of children and a list of FamilyMemberDTO, return an empty list if no children at the address
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertByAddress(@RequestParam("address") String address){
        return personSpecificInfoService.getChildAlertByAddress(address);
    }
}
