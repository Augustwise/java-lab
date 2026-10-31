package org.example.lab9.task1;

import java.util.Objects;

public class Bank {
    public void transfer(Account from, Account to, int amount) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (amount < 0) {
            throw new IllegalArgumentException("Сума переказу не може бути від'ємною.");
        }
        if (from == to || amount == 0) {
            return;
        }

        // Усі потоки блокують рахунки в одному порядку, щоб уникнути deadlock.
        Account first = from.getId() < to.getId() ? from : to;
        Account second = from.getId() < to.getId() ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.getBalance() < amount) {
                    return;
                }

                // Перевіряємо переповнення до списання грошей.
                Math.addExact(to.getBalance(), amount);
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
