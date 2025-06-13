package com.safetynet.safetynet_alerts.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;


public class AgeUtils {

    private static final Logger log = LoggerFactory.getLogger(AgeUtils.class);

    public static long countNumberOfAdults(List<LocalDate> birthdates) {
        long numberOfAdults = birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() > 18).count();
        log.debug("Le nombre d'adultes présents dans cette liste de dates de naissances : {} est de {}.", birthdates, numberOfAdults);
        return numberOfAdults;
    }


    public static long countNumberOfChildren(List<LocalDate> birthdates) {
        long numberOfChildren = birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() <= 18).count();
        log.debug("Le nombre d'enfants présents dans cette liste de dates de naissances : {} est de {}.", birthdates, numberOfChildren);
        return numberOfChildren;
    }
}
