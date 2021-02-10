package com.safetynet.alerts.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class InputDataReader {

    private static final Logger log = LogManager.getLogger(InputDataReader.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private AlertsData alertsData;

    public AlertsData loadData(String filePath){
        log.debug("Loading lists from \"" + filePath + "\"");
        try {
            alertsData = objectMapper.readValue(new File(filePath), AlertsData.class);
        } catch (IOException e) {
            log.error("Error parsing input file to build lists", e);
        }
        return alertsData;
    }

}
