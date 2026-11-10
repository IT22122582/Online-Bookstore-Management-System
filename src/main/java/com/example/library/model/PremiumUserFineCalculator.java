package com.example.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PremiumUserFineCalculator implements FineCalculator {
    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long days = ChronoUnit.DAYS.between(dueDate, returnDate);
            if (days <= 7) {
                return 0.0; // 7-day grace period
            } else {
                return (days - 7) * 0.10; // $0.10 per day after grace period
            }
        }
        return 0.0;
    }
}
