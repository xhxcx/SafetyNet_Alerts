package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.AlertsData;

public interface AlertsDataOutputWriter {
    void writeAsJsonIntoFile(AlertsData dataObject, String filePath);
}
