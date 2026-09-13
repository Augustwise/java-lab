package org.example.lab3.task2;

import java.time.LocalDate;
/**
 * Проїзна картка, обмежена за терміном дії (на 10 днів або на місяць).
 */
public class TimeTravelCard extends TravelCard {
    private final LocalDate expirationDate;

    public TimeTravelCard(
            String id,
            PassengerType passengerType,
            LocalDate expirationDate) {
        super(id, passengerType);
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean canPayForTrip(LocalDate currentDate) {
        return !currentDate.isAfter(expirationDate);
    }

    @Override
    public void payForTrip() {
        // Кількість поїздок за цією карткою необмежена до завершення її дії.
    }
}
