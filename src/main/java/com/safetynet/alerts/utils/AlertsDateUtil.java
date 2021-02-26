package com.safetynet.alerts.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Component utils to manage date transformations
 *
 */
@Component
public class AlertsDateUtil {

    /**
     * Get local date when called
     *
     * @return instant date
     */
    public LocalDate getCurrentLocalDate(){
        return LocalDate.now();
    }

    /**
     * Transform string date at format mm/dd/yyyy into LocalDate
     * @param dateToRead String with format mm/dd/yyyy
     * @return LocalDate
     */
    public LocalDate readDateFromString(String dateToRead){
        return LocalDate.parse(dateToRead, DateTimeFormatter.ofPattern("MM/dd/uuuu"));
    }

    /**
     * Calculate the period between a start date and now
     *
     * @param birthDate to set start date
     * @return the age in years or -1 if birthdate is invalid
     */
    public int calculateAge(LocalDate birthDate){
        if(birthDate != null) {
            return Period.between(birthDate, getCurrentLocalDate()).getYears();
        }
        else
            return -1;
    }
}
