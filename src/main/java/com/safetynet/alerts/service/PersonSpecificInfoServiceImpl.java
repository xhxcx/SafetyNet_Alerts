package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.ChildDTO;
import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.AlertsDateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonSpecificInfoServiceImpl implements PersonSpecificInfoService {

    private static final Logger log = LogManager.getLogger(PersonSpecificInfoService.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();
        List<Person> personList = personRepository.getPersons()
                .stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)&&p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if(!personList.isEmpty()){
            for(Person p : personList) {
                PersonInfoDTO personInfoDTO = new PersonInfoDTO();
                MedicalRecord record = medicalRecordRepository.getMedicalRecordByName(p.getFirstName(),p.getLastName());
                personInfoDTO.setLastName(p.getLastName());
                personInfoDTO.setAddress(p.getAddress());
                personInfoDTO.setAge(new AlertsDateUtil().calculateAge(LocalDate.parse(record.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/uuuu"))));
                personInfoDTO.setEmail(p.getEmail());
                personInfoDTO.setMedications(record.getMedications());
                personInfoDTO.setAllergies(record.getAllergies());
                personInfoDTOList.add(personInfoDTO);
            }
        }
        else{
            log.info("Person Info : nobody found for : " + firstName + " - " + lastName);
            return new ArrayList<>();
        }
        return personInfoDTOList;
    }


    /**
     * Get a list of all children at an address and their family members at the same address
     * Will return a ChildAlertDTO list that is composed by a list of ChildDTO and a list of FamilyMemberDTO
     *
     * @param address
     * @return List of ChildAlertDTO if at least a child found at the address, else return empty list.
     */
    @Override
    public List<ChildAlertDTO> getChildAlertByAddress(String address) {
        List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();
        //Get persons from address
        List<Person> globalPersonList = personRepository.getPersonsByAddress(address);
        //Group by lastName to build family as person list
        Map<String,List<Person>> personsGroupedByName = globalPersonList.stream().collect(Collectors.groupingBy(Person::getLastName));

        //Parse each family to build child list and family member list
        personsGroupedByName
                .forEach((key, value) -> {
                    ChildAlertDTO childAlertDTO = new ChildAlertDTO();
                    List<ChildDTO> childDTOList = new ArrayList<>();
                    List<FamilyMemberDTO> familyMemberDTOList = new ArrayList<>();
                    // parse person list of the family to filter children into ChildDTO and other members to FamilyMemberDTO
                    value.forEach(person -> {
                        MedicalRecord personRecord = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());
                        if (personRecord != null) {
                            int age = new AlertsDateUtil().calculateAge(new AlertsDateUtil().readDateFromString(personRecord.getBirthdate()));
                            if (age <= 18) {
                                ChildDTO child = processPersonIntoChild(person, age);
                                childDTOList.add(child);
                            } else {
                                FamilyMemberDTO familyMember = new FamilyMemberDTO();
                                familyMember.setFirstName(person.getFirstName());
                                familyMember.setLastName(person.getLastName());
                                familyMemberDTOList.add(familyMember);
                            }
                        }
                    });
                    // if there is at least one children into the family => set ChildAlertDTO for this family then add it to the returned list
                    if (childDTOList.size() > 0) {
                        childAlertDTO.setChildList(childDTOList);
                        childAlertDTO.setFamilyMemberList(familyMemberDTOList);
                        childAlertDTOList.add(childAlertDTO);
                    }
                });

        if(childAlertDTOList.size() == 0)
            return new ArrayList<>();
        else
            return childAlertDTOList;
    }

    /**
     * Transform a person into a ChildDTO
     * @param person
     * @param age
     * @return a ChildDTO
     */
    private ChildDTO processPersonIntoChild(Person person, int age){
        ChildDTO child = new ChildDTO();
        child.setFirstName(person.getFirstName());
        child.setLastName(person.getLastName());
        child.setAge(age);
        return child;
    }
}
