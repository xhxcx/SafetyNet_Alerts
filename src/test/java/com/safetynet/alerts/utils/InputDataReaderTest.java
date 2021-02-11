package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.AlertsData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class InputDataReaderTest {

    private InputDataReader inputDataReaderUT = new InputDataReader();

    private String testFilePath = "./src/test/resources/data-test.json";

    @Test
    public void loadDataTest(){
        AlertsData alertsData = inputDataReaderUT.loadData(testFilePath);
        //Person asserts
        assertThat(alertsData.getPersons().size()).isEqualTo(23);
        assertThat(alertsData.getPersons().get(0).getFirstName()).isEqualTo("John");

        //FireStation assert
        assertThat(alertsData.getFirestations().size()).isEqualTo(13);
        assertThat(alertsData.getFirestations().get(0).getStation()).isEqualTo(3);

        //MedicalRecord assert
        assertThat(alertsData.getMedicalrecords().size()).isEqualTo(23);
        assertThat(alertsData.getMedicalrecords().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void loadDataErrorTest(){
        AlertsData alertsData = inputDataReaderUT.loadData("Not Found");
        assertThat(alertsData).isNull();
    }

}
