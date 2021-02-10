package com.safetynet.alerts.config;

import com.safetynet.alerts.model.AlertsData;
import com.safetynet.alerts.utils.InputDataReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AlertsConfig {

    @Value("${dataFilePath}")
    private String dataFilePath;

    /**
     * At the initialization of the application create a bean containing data read as POJO
     *
     * @return AlertsData object with personList, firestationsList and medicalrecordsList
     */
    @Bean
    @PostConstruct
    public AlertsData getAlertsDatas(){return new InputDataReader().loadData(dataFilePath);}


}
