package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
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
}
