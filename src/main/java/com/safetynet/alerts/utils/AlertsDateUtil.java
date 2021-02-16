package com.safetynet.alerts.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

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
     * Calculate the period between a start date and now
     *
     * @param birthDate to set start date
     * @return the age in years or -1 if birthdate is invalid
     */
    public int calculateAge(LocalDate birthDate){
        if(null != birthDate) {
            return Period.between(birthDate, getCurrentLocalDate()).getYears();
        }
        else
            return -1;
    }
}
