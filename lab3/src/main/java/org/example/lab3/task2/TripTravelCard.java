package org.example.lab3.task2;

import java.time.LocalDate;
/**
 * Проїзна картка на лімітовану кількість поїздок (на 5 або 10).
 */
public class TripTravelCard extends TravelCard {
    private int remainingTrips;

    public TripTravelCard(
            String id,
            PassengerType passengerType,
            int trips) {
        super(id, passengerType);
        this.remainingTrips = trips;
    }

    public int getRemainingTrips() {
        return remainingTrips;
    }

    @Override
    public boolean canPayForTrip(LocalDate currentDate) {
        return remainingTrips > 0;
    }

    @Override
    public void payForTrip() {
        remainingTrips--;
    }
}
