package org.example.lab3.task2;

import java.time.LocalDate;

// Проїзна карта
public abstract class TravelCard {
    private final String id;
    private final PassengerType passengerType;

    protected TravelCard(String id, PassengerType passengerType) {
        this.id = id;
        this.passengerType = passengerType;
    }

    public String getId() {
        return id;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public abstract boolean canPayForTrip(LocalDate currentDate);

    public abstract void payForTrip();
}
