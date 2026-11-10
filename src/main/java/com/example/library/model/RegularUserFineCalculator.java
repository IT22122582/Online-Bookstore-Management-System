package com.example.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegularUserFineCalculator implements FineCalculator {
    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long days = ChronoUnit.DAYS.between(dueDate, returnDate);
            return days * 0.50; // $0.50 per day
        }
        return 0.0;
    }
}
