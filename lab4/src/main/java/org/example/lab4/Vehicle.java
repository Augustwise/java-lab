package org.example.lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Vehicle<P extends Person> {
    private final int capacity;
    private final List<P> passengers = new ArrayList<>();

    protected Vehicle(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupiedSeats() {
        return passengers.size();
    }

    public List<P> getPassengers() {
        return Collections.unmodifiableList(passengers);
    }

    // Посадка пассажира
    public void board(P passenger) {
        Objects.requireNonNull(passenger, "Passenger must not be null");
        if (passengers.size() == capacity) {
            throw new TransportFullException(getClass().getSimpleName() + " has no free seats");
        }
        passengers.add(passenger);
    }
    // Висадка
    public void disembark(P passenger) {
        Objects.requireNonNull(passenger, "Passenger must not be null");
        if (!passengers.remove(passenger)) {
            throw new PassengerNotFoundException(passenger + " is not in " + getClass().getSimpleName());
        }
    }
}
