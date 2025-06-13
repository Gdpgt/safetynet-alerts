package com.safetynet.safetynet_alerts.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static int calculateAge(LocalDate birthdate) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        log.debug("L'âge lié à la date de naissance {} est {}.", birthdate, age);
        return age;
    }
}
