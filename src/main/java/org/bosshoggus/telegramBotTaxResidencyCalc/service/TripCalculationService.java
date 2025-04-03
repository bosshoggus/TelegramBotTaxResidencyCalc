package org.bosshoggus.telegramBotTaxResidencyCalc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class TripCalculationService {
    private final List<Long> daysOfTheTrips;
    final long MAX_RESEDENCY_DAYS = 183;
    final long DAYS_IN_12_MONTH = ChronoUnit.DAYS.between(LocalDate.now().minusMonths(12), LocalDate.now());
    final LocalDate startOfTheTaxResidencyPeriod = LocalDate.now().minusMonths(12);
    final LocalDate endtOfTheTaxResidencyPeriod = LocalDate.now();

    @Autowired
    public TripCalculationService(List<Long> daysOfTheTrips) {
        this.daysOfTheTrips = daysOfTheTrips;
    }

    public void addRidesToList(LocalDate startDate, LocalDate endDate) {
        long howMuchDays;
        if (!(endDate.isBefore(startOfTheTaxResidencyPeriod) || startDate.isAfter(endtOfTheTaxResidencyPeriod))) {
            if (startDate.isBefore(startOfTheTaxResidencyPeriod)) {
                howMuchDays = ChronoUnit.DAYS.between(startOfTheTaxResidencyPeriod, endDate);
                daysOfTheTrips.add(howMuchDays);
            } else if (endDate.isAfter(endtOfTheTaxResidencyPeriod)) {
                howMuchDays = ChronoUnit.DAYS.between(startDate, endtOfTheTaxResidencyPeriod);
                daysOfTheTrips.add(howMuchDays);
            } else {
                daysOfTheTrips.add(ChronoUnit.DAYS.between(startDate, endDate));
            }
        }
    }

    public long getDaysCountFoFreedom() {
        long sumDaysOfTheTrip = -1L;
        for (Long l : daysOfTheTrips) {
            sumDaysOfTheTrip = sumDaysOfTheTrip + l;
        }
        return DAYS_IN_12_MONTH - MAX_RESEDENCY_DAYS - sumDaysOfTheTrip;
    }
}
