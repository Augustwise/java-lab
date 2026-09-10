package org.example.lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Road {
    private final List<Automobile<? extends Person>> carsInRoad = new ArrayList<>();

    public void addCarToRoad(Automobile<? extends Person> car) {
        carsInRoad.add(Objects.requireNonNull(car, "Car must not be null"));
    }

    public List<Automobile<? extends Person>> getCarsInRoad() {
        return Collections.unmodifiableList(carsInRoad);
    }

    public int getCountOfHumans() {
        return carsInRoad.stream()
                .mapToInt(Vehicle::getOccupiedSeats)
                .sum();
    }
}
