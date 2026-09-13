package org.example.lab4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleTest {
    @Test
    // Bus і Taxi мають приймати пасажирів будь-якого типу (Person, Firefighter, PoliceOfficer)
    void busAndTaxiAcceptAllPassengerKinds() {
        Bus bus = new Bus(3);
        Taxi taxi = new Taxi(3);

        Person person = new Person("Oleh");
        Firefighter firefighter = new Firefighter("Olha");
        PoliceOfficer policeOfficer = new PoliceOfficer("Taras");
        bus.board(person);
        bus.board(firefighter);
        taxi.board(policeOfficer);

        assertEquals(2, bus.getOccupiedSeats());
        assertEquals(1, taxi.getOccupiedSeats());
        assertTrue(bus.getPassengers().contains(firefighter));
    }

    @Test
    // FireTruck приймає лише Firefighter, а PoliceCar — лише PoliceOfficer (перевірка на рівні типів)
    void specializedAutomobilesAcceptTheirRequiredPassengerType() {
        FireTruck fireTruck = new FireTruck(2);
        PoliceCar policeCar = new PoliceCar(2);
        Firefighter firefighter = new Firefighter("Ira");
        PoliceOfficer policeOfficer = new PoliceOfficer("Andrii");

        fireTruck.board(firefighter);
        policeCar.board(policeOfficer);

        assertEquals(1, fireTruck.getOccupiedSeats());
        assertEquals(1, policeCar.getOccupiedSeats());
        // fireTruck.board(policeOfficer) and policeCar.board(firefighter) do not compile.
    }

    @Test
    // Посадка в заповнений транспорт (усі місця зайняті) має кидати TransportFullException
    void boardingIntoFullVehicleThrowsException() {
        Taxi taxi = new Taxi(1);
        taxi.board(new Person("Oleh"));

        assertThrows(TransportFullException.class, () -> taxi.board(new Person("Ira")));
    }

    @Test
    // Висадка пасажира, якого немає в салоні, кидає PassengerNotFoundException;
    // висадка реального пасажира коректно зменшує кількість місць
    void disembarkingAbsentPassengerThrowsException() {
        Bus bus = new Bus(2);
        Person aboard = new Person("Oleh");
        Person absent = new Person("Ira");
        bus.board(aboard);

        assertThrows(PassengerNotFoundException.class, () -> bus.disembark(absent));
        bus.disembark(aboard);
        assertEquals(0, bus.getOccupiedSeats());
        assertFalse(bus.getPassengers().contains(aboard));
    }

    @Test
    // Road коректно рахує пасажирів у різних авто (з різними generic-типами пасажирів) на дорозі
    void roadCountsPassengersInCarsWithDifferentGenericTypes() {
        Taxi taxi = new Taxi(3);
        FireTruck fireTruck = new FireTruck(2);
        PoliceCar policeCar = new PoliceCar(1);
        taxi.board(new Person("Oleh"));
        taxi.board(new Firefighter("Ira"));
        fireTruck.board(new Firefighter("Petro"));
        policeCar.board(new PoliceOfficer("Anna"));

        Road road = new Road();
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        assertEquals(4, road.getCountOfHumans());
        assertEquals(3, road.getCarsInRoad().size());
    }
}
