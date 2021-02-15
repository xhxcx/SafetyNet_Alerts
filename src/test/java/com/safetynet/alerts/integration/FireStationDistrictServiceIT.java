package com.safetynet.alerts.integration;

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
}
