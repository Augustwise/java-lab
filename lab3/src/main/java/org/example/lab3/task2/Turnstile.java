package org.example.lab3.task2;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;


/**
 * Турнікет
 */
public class Turnstile {
    private final CardRegistry registry;
    private int allowedPassages;
    private int deniedPassages;
    private final Map<PassengerType, Integer> allowedByType = new EnumMap<>(PassengerType.class);
    private final Map<PassengerType, Integer> deniedByType = new EnumMap<>(PassengerType.class);

    public Turnstile(CardRegistry registry) {
        this.registry = registry;
        // Ініціалізуємо статистику по типам пасажирів
        for (PassengerType type : PassengerType.values()) {
            allowedByType.put(type, 0);
            deniedByType.put(type, 0);
        }
    }

    public boolean tryToPass(TravelCard card, LocalDate currentDate) {
        if (!registry.contains(card)) {
            deniedPassages++;
            return false;
        }

        PassengerType type = card.getPassengerType();
        if (!card.canPayForTrip(currentDate)) {
            deniedPassages++;
            deniedByType.put(type, deniedByType.get(type) + 1);
            return false;
        }

        card.payForTrip();
        allowedPassages++;
        allowedByType.put(type, allowedByType.get(type) + 1);
        return true;
    }

    public int getAllowedPassages() {
        return allowedPassages;
    }

    public int getDeniedPassages() {
        return deniedPassages;
    }

    public int getAllowedPassages(PassengerType type) {
        return allowedByType.get(type);
    }

    public int getDeniedPassages(PassengerType type) {
        return deniedByType.get(type);
    }
}
