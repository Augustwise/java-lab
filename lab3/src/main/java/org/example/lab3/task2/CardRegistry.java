package org.example.lab3.task2;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CardRegistry {
    private final Map<String, TravelCard> cards = new HashMap<>();

    public TimeTravelCard issueTimeCard(
            String id,
            PassengerType passengerType,
            ValidityPeriod period,
            LocalDate startDate) {
        checkNewId(id);
        LocalDate expirationDate = period.calculateExpirationDate(startDate);
        TimeTravelCard card = new TimeTravelCard(id, passengerType, expirationDate);
        cards.put(id, card);
        return card;
    }

    public TripTravelCard issueTripCard(
            String id,
            PassengerType passengerType,
            int trips) {
        if (trips != 5 && trips != 10) {
            throw new IllegalArgumentException("Картка на поїздки повинна містити 5 або 10 поїздок");
        }
        checkNewId(id);
        TripTravelCard card = new TripTravelCard(id, passengerType, trips);
        cards.put(id, card);
        return card;
    }

    public BalanceTravelCard issueBalanceCard(
            String id,
            double initialBalance,
            double fare) {
        if (initialBalance < 0 || fare <= 0) {
            throw new IllegalArgumentException("Некоректні значення балансу або вартості проїзду");
        }
        checkNewId(id);
        BalanceTravelCard card = new BalanceTravelCard(id, initialBalance, fare);
        cards.put(id, card);
        return card;
    }

    public boolean contains(TravelCard card) {
        return card != null && cards.get(card.getId()) == card;
    }

    private void checkNewId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ідентифікатор картки не може бути порожнім");
        }
        if (cards.containsKey(id)) {
            throw new IllegalArgumentException("Картка з таким ідентифікатором уже існує");
        }
    }
}
