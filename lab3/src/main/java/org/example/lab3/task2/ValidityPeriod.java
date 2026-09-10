package org.example.lab3.task2;

import java.time.LocalDate;

public enum ValidityPeriod {
    TEN_DAYS,
    MONTH;

    public LocalDate calculateExpirationDate(LocalDate startDate) {
        if (this == TEN_DAYS) {
            return startDate.plusDays(9);
        }
        return startDate.plusMonths(1).minusDays(1);
    }
}
