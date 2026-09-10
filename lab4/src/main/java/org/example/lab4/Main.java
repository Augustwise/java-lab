package org.example.lab4;

public class Main {
    public static void main(String[] args) {
        Bus bus = new Bus(3);
        Taxi taxi = new Taxi(2);
        FireTruck fireTruck = new FireTruck(2);
        PoliceCar policeCar = new PoliceCar(1);

        bus.board(new Person("Oksana"));
        bus.board(new Firefighter("Ihor"));
        taxi.board(new PoliceOfficer("Anna"));
        fireTruck.board(new Firefighter("Petro"));
        policeCar.board(new PoliceOfficer("Maksym"));

        Road road = new Road();
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        System.out.println("People in automobiles on the road: " + road.getCountOfHumans());
        System.out.println("People in bus: " + bus.getOccupiedSeats());
    }
}
