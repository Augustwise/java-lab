package org.example.lab3.task2;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnstileTest {
    private final LocalDate today = LocalDate.of(2026, 9, 11);

    @Test
    void allowsPassAndUsesOneTrip() {
        CardRegistry registry = new CardRegistry();
        TripTravelCard card = registry.issueTripCard(
                "ST-1",
                PassengerType.STUDENT,
                5);
        Turnstile turnstile = new Turnstile(registry);

        assertTrue(turnstile.tryToPass(card, today));
        assertEquals(4, card.getRemainingTrips());
        assertEquals(1, turnstile.getAllowedPassages());
        assertEquals(1, turnstile.getAllowedPassages(PassengerType.STUDENT));
    }

    @Test
    void deniesExpiredCard() {
        CardRegistry registry = new CardRegistry();
        TimeTravelCard card = registry.issueTimeCard(
                "S-1",
                PassengerType.SCHOOL,
                ValidityPeriod.TEN_DAYS,
                today.minusDays(11));
        Turnstile turnstile = new Turnstile(registry);

        assertFalse(turnstile.tryToPass(card, today));
        assertEquals(1, turnstile.getDeniedPassages());
        assertEquals(1, turnstile.getDeniedPassages(PassengerType.SCHOOL));
    }

    @Test
    void tenDayCardIsValidForExactlyTenCalendarDays() {
        CardRegistry registry = new CardRegistry();
        TimeTravelCard card = registry.issueTimeCard(
                "S-2",
                PassengerType.SCHOOL,
                ValidityPeriod.TEN_DAYS,
                today);
        Turnstile turnstile = new Turnstile(registry);

        assertTrue(turnstile.tryToPass(card, today.plusDays(9)));
        assertFalse(turnstile.tryToPass(card, today.plusDays(10)));
    }

    @Test
    void balanceCardHasNoExpirationAndChargesFare() {
        CardRegistry registry = new CardRegistry();
        BalanceTravelCard card = registry.issueBalanceCard("R-1", 30, 15);
        Turnstile turnstile = new Turnstile(registry);

        assertTrue(turnstile.tryToPass(card, today.plusYears(5)));
        assertTrue(turnstile.tryToPass(card, today.plusYears(5)));
        assertFalse(turnstile.tryToPass(card, today.plusYears(5)));
        assertEquals(0, card.getBalance());

        card.topUp(15);
        assertTrue(turnstile.tryToPass(card, today.plusYears(5)));
    }

    @Test
    void deniesUnreadableAndUnregisteredCards() {
        CardRegistry registry = new CardRegistry();
        Turnstile turnstile = new Turnstile(registry);
        TripTravelCard unregisteredCard = new TripTravelCard(
                "UNKNOWN",
                PassengerType.REGULAR,
                5);

        assertFalse(turnstile.tryToPass(null, today));
        assertFalse(turnstile.tryToPass(unregisteredCard, today));
        assertEquals(2, turnstile.getDeniedPassages());
    }

    @Test
    void registryOnlyIssuesSupportedCards() {
        CardRegistry registry = new CardRegistry();

        assertThrows(IllegalArgumentException.class,
                () -> registry.issueTripCard("A", PassengerType.REGULAR, 7));

        registry.issueTripCard("A", PassengerType.REGULAR, 5);
        assertThrows(IllegalArgumentException.class,
                () -> registry.issueTripCard("A", PassengerType.STUDENT, 10));
    }
}
