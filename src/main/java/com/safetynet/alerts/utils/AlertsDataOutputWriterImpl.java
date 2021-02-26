package com.safetynet.alerts.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.AlertsData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AlertsDataOutputWriterImpl implements AlertsDataOutputWriter{

    private static final Logger log = LogManager.getLogger(AlertsDataOutputWriter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void writeAsJsonIntoFile(AlertsData dataObject, String filePath) {
        if (dataObject != null) {
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(fos, dataObject);
                log.info("JSON file update :: SUCCEED");
            } catch (JsonProcessingException | FileNotFoundException e) {
                log.error("JSON file update :: FAILED " + e);
            } catch (IOException e) {
                log.error("JSON file update :: FAILED " + e);
            }
        }
        else
            log.error("JSON file update :: FAILED / Can't write null object");
    }
}
