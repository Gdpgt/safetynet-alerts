package com.safetynet.safetynet_alerts.utils;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    public static int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }
}
