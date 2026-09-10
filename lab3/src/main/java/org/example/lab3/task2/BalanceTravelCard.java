package org.example.lab3.task2;

import java.time.LocalDate;

public class BalanceTravelCard extends TravelCard {
    private double balance;
    private final double fare;

    public BalanceTravelCard(
            String id,
            double initialBalance,
            double fare) {
        super(id, PassengerType.REGULAR);
        this.balance = initialBalance;
        this.fare = fare;
    }

    public double getBalance() {
        return balance;
    }

    public void topUp(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сума поповнення має бути додатною");
        }
        balance += amount;
    }

    @Override
    public boolean canPayForTrip(LocalDate currentDate) {
        return balance >= fare;
    }

    @Override
    public void payForTrip() {
        balance -= fare;
    }
}
