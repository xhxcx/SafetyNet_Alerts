package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonSpecificInfoServiceImpl;
import com.safetynet.alerts.utils.AlertsDateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class PersonSpecificInfoServiceIT {

    @Autowired
    private PersonSpecificInfoServiceImpl personSpecificInfoServiceUT;

    @Test
    public void PersonSpecificInfoService_shouldReturnAPersonInfoDTOList_whenGetPersonInfo(){
        List<PersonInfoDTO> personInfoDTOList = personSpecificInfoServiceUT.getPersonInfo("John", "Boyd");
        PersonInfoDTO expectedPersonInfoDTO = new PersonInfoDTO();
        expectedPersonInfoDTO.setLastName("Boyd");
        expectedPersonInfoDTO.setAddress("1509 Culver St");
        expectedPersonInfoDTO.setAge(new AlertsDateUtil().calculateAge(LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/uuuu"))));
        expectedPersonInfoDTO.setEmail("jaboyd@email.com");
        expectedPersonInfoDTO.setMedications(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")));
        expectedPersonInfoDTO.setAllergies(new ArrayList<>(Arrays.asList("nillacilan")));

        assertThat(personInfoDTOList.size()).isEqualTo(1);
        assertThat(personInfoDTOList.contains(expectedPersonInfoDTO)).isTrue();
    }
}
