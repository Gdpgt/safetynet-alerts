package com.safetynet.safetynet_alerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class AgeUtils {

    public static long countNumberOfAdults(List<LocalDate> birthdates) {
        return birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() > 18).count();
    }


    public static long countNumberOfChildren(List<LocalDate> birthdates) {
        return birthdates.stream().filter(b -> Period.between(b, LocalDate.now()).getYears() <= 18).count();
    }
}
