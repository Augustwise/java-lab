package org.example.lab9.task1;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final int ACCOUNT_COUNT = 100;
    private static final int THREAD_COUNT = 3_000;
    private static final int TRANSFERS_PER_THREAD = 100;

    public static void main(String[] args) throws InterruptedException {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Bank bank = new Bank();
        Account[] accounts = new Account[ACCOUNT_COUNT];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(ThreadLocalRandom.current().nextInt(1_000, 10_001));
        }

        long before = totalBalance(accounts);
        System.out.println("Кількість рахунків: " + ACCOUNT_COUNT);
        System.out.println("Кількість потоків: " + THREAD_COUNT);
        System.out.println("Сума до переказів: " + before);

        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[THREAD_COUNT];
        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        start.await();
                        ThreadLocalRandom random = ThreadLocalRandom.current();
                        for (int j = 0; j < TRANSFERS_PER_THREAD; j++) {
                            Account from = accounts[random.nextInt(accounts.length)];
                            Account to = accounts[random.nextInt(accounts.length)];
                            int amount = random.nextInt(1, 1_001);
                            bank.transfer(from, to, amount);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, "Transfer-" + (i + 1));
                threads[i].start();
            }
        } finally {
            start.countDown();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long after = totalBalance(accounts);
        for (Account account : accounts) {
            if (account.getBalance() < 0) {
                throw new IllegalStateException("Виявлено від'ємний баланс.");
            }
        }
        System.out.println("Сума після переказів: " + after);
        if (before != after) {
            throw new IllegalStateException("Загальна сума грошей змінилася.");
        }
        System.out.println("Перевірку пройдено: сума збережена, від'ємних балансів немає.");
    }

    private static long totalBalance(Account[] accounts) {
        long total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
}
