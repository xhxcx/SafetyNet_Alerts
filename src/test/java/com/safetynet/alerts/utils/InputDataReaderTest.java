package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class InputDataReaderTest {

    private InputDataReader inputDataReaderUT = new InputDataReader();

    private String testFilePath = "./src/test/resources/data.json";

    @Test
    public void readPersonsFromDataFileTest(){
        List<Person> allPersons = inputDataReaderUT.getAllPersonsFromFile(testFilePath);
        assertThat(allPersons.size()).isEqualTo(23);
        assertThat(allPersons.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void readFireStationsFromDataFileTest(){
        List<FireStation> allFireStations = inputDataReaderUT.getAllStationsFromFile(testFilePath);
        assertThat(allFireStations.size()).isEqualTo(13);
        assertThat(allFireStations.get(0).getStationNumber()).isEqualTo(3);
    }

    @Test
    public void readMedicalRecordsFromDataFileTest(){
        List<MedicalRecord> allMedicalRecords = inputDataReaderUT.getAllMedicalRecordsFromFile(testFilePath);
        assertThat(allMedicalRecords.size()).isEqualTo(23);
        assertThat(allMedicalRecords.get(0).getFirstName()).isEqualTo("John");
    }

    @Nested
    class ReadTestErrors{
        @Test
        public void readPersonsFromDataFileErrorTest(){
            List<Person> allPersons = inputDataReaderUT.getAllPersonsFromFile("notFound");
            assertThat(allPersons.size()).isEqualTo(0);
            assertThatIOException();
        }

        @Test
        public void readStationsFromDataFileErrorTest(){
            List<FireStation> allStations = inputDataReaderUT.getAllStationsFromFile("notFound");
            assertThat(allStations.size()).isEqualTo(0);
            assertThatIOException();
        }

        @Test
        public void readMedicalRecordsFromDataFileErrorTest(){
            List<MedicalRecord> allMedicalRecords = inputDataReaderUT.getAllMedicalRecordsFromFile("notFound");
            assertThat(allMedicalRecords.size()).isEqualTo(0);
            assertThatIOException();
        }
    }

}
