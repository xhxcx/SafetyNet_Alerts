package com.safetynet.alerts.config;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import com.safetynet.alerts.utils.InputDataReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class AlertsConfig {

    @Value("${dataFilePath}")
    private String dataFilePath;

    /**
     * At the initialization of the application create a bean from the list of persons read in the data file
     *
     * @return the list of all persons in the file
     *
     */
    @Bean
    @Qualifier("listOfPersons")
    @PostConstruct
    public List<Person> getAllPersonsFromJSON() {
        return new InputDataReader().getAllPersonsFromFile(dataFilePath);
    }

    /**
     * At the initialization of the application create a bean from the list of fire stations read in the data file
     *
     * @return the list of all stations in the file
     *
     */
    @Bean
    @Qualifier("listOfFireStations")
    @PostConstruct
    public List<FireStation> getAllFireStationsFromJSON() {
        return new InputDataReader().getAllStationsFromFile(dataFilePath);
    }

    /**
     * At the initialization of the application create a bean from the list of medical records read in the data file
     *
     * @return the list of all medical records in the file
     *
     */
    @Bean
    @Qualifier("listOfMedicalRecords")
    @PostConstruct
    public List<MedicalRecord> getAllMedicalRecordsFromJSON() {
        return new InputDataReader().getAllMedicalRecordsFromFile(dataFilePath);
    }

}
