package org.example.lab9.task1;

import java.util.concurrent.atomic.AtomicLong;

public final class Account {
    private static final AtomicLong NEXT_ID = new AtomicLong();

    private final long id = NEXT_ID.getAndIncrement();
    private long balance;

    public Account(long balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Баланс не може бути від'ємним.");
        }
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public synchronized long getBalance() {
        return balance;
    }

    synchronized void withdraw(int amount) {
        if (amount < 0 || amount > balance) {
            throw new IllegalArgumentException("Неприпустима сума зняття.");
        }
        balance -= amount;
    }

    synchronized void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сума не може бути від'ємною.");
        }
        balance = Math.addExact(balance, amount);
    }
}
