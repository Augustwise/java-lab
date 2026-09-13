package org.example.lab3.task2;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        CardRegistry registry = new CardRegistry();
        Turnstile turnstile = new Turnstile(registry);

        TimeTravelCard schoolCard = registry.issueTimeCard(
                "S-101",
                PassengerType.SCHOOL,
                ValidityPeriod.MONTH,
                today);
        TripTravelCard studentCard = registry.issueTripCard(
                "ST-205",
                PassengerType.STUDENT,
                5);
        BalanceTravelCard regularCard = registry.issueBalanceCard(
                "R-310",
                60,
                15);

        printResult("Шкільна місячна картка", turnstile.tryToPass(schoolCard, today));
        printResult("Студентська картка на 5 поїздок", turnstile.tryToPass(studentCard, today));
        printResult("Звичайна картка з балансом", turnstile.tryToPass(regularCard, today));

        // Студент намагається пройти ще 5 разів (хоча залишилось лише 4 поїздки)
        for (int i = 0; i < 5; i++) {
            turnstile.tryToPass(studentCard, today);
        }
        printResult("Нечитабельна картка", turnstile.tryToPass(null, today));

        System.out.println("\nУсього дозволених проходів: " + turnstile.getAllowedPassages());
        System.out.println("Усього відхилених проходів: " + turnstile.getDeniedPassages());

        for (PassengerType type : PassengerType.values()) {
            System.out.println(
                    type.getUkrainianName() + ": дозволено = " + turnstile.getAllowedPassages(type)
                            + ", відхилено = " + turnstile.getDeniedPassages(type));
        }
    }

    private static void printResult(String cardName, boolean allowed) {
        System.out.println(cardName + ": " + (allowed ? "прохід дозволено" : "прохід заборонено"));
    }
}
