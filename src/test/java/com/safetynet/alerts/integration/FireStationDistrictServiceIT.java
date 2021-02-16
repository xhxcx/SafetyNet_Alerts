package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.CoveredPersonDTO;
import com.safetynet.alerts.service.FireStationDistrictServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class FireStationDistrictServiceIT {

    @Autowired
    private FireStationDistrictServiceImpl fireStationDistrictServiceUT;


    @Test
    public void fireStationDistrictService_shouldReturnAListOfPhones_whenGetPhonesByStationNumber() {
        List<String> expectedPhones = new ArrayList<>();
        expectedPhones.add("841-874-6874");
        expectedPhones.add("841-874-9845");
        expectedPhones.add("841-874-8888");
        expectedPhones.add("841-874-9888");

        assertThat(fireStationDistrictServiceUT.getPhonesByStationNumber(4).size()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getPhonesByStationNumber(4).containsAll(expectedPhones)).isTrue();
    }

    @Test
    public void fireStationDistrictService_shouldReturnAListCoveredPerson_andACountRegardingPersonAge_whenGetFireStationDistrictCoverage() {
        CoveredPersonDTO expectedPerson = new CoveredPersonDTO();
        expectedPerson.setFirstName("Tony");
        expectedPerson.setLastName("Cooper");
        expectedPerson.setPhone("841-874-6874");
        expectedPerson.setAddress("112 Steppes Pl");

        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getCoveredPersonDTOList().size()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getChildrenCount()).isEqualTo(0);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getAdultCount()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getCoveredPersonDTOList().contains(expectedPerson)).isTrue();
    }
}
