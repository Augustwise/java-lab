package org.example.lab9.task1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private final Bank bank = new Bank();
    private final ExecutorService executor = Executors.newCachedThreadPool(task -> {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
    });

    @AfterEach
    void stopThreads() {
        executor.shutdownNow();
    }

    @Test
    void transfersMoney() {
        Account from = new Account(100);
        Account to = new Account(50);
        bank.transfer(from, to, 40);
        assertEquals(60, from.getBalance());
        assertEquals(90, to.getBalance());
    }

    @Test
    void skipsTransferWhenMoneyIsInsufficient() {
        Account from = new Account(20);
        Account to = new Account(50);
        bank.transfer(from, to, 30);
        assertEquals(20, from.getBalance());
        assertEquals(50, to.getBalance());
    }

    @Test
    void handlesSameAccountAndZeroAmount() {
        Account from = new Account(100);
        Account to = new Account(50);
        bank.transfer(from, from, 200);
        bank.transfer(from, to, 0);
        assertEquals(100, from.getBalance());
        assertEquals(50, to.getBalance());
    }

    @Test
    void rejectsNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> new Account(-1));
        Account from = new Account(100);
        Account to = new Account(50);
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(from, to, -1));
        assertEquals(100, from.getBalance());
        assertEquals(50, to.getBalance());
    }

    @Test
    void overflowDoesNotLoseMoney() {
        Account from = new Account(100);
        Account to = new Account(Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> bank.transfer(from, to, 1));
        assertEquals(100, from.getBalance());
        assertEquals(Long.MAX_VALUE, to.getBalance());
    }

    @Test
    void oppositeTransfersFinishWithoutDeadlock() throws Exception {
        Account first = new Account(10_000);
        Account second = new Account(10_000);
        CountDownLatch start = new CountDownLatch(1);
        Future<?> forward = executor.submit(() -> {
            start.await();
            for (int i = 0; i < 10_000; i++) {
                bank.transfer(first, second, 1);
            }
            return null;
        });
        Future<?> backward = executor.submit(() -> {
            start.await();
            for (int i = 0; i < 10_000; i++) {
                bank.transfer(second, first, 1);
            }
            return null;
        });
        start.countDown();
        forward.get(10, TimeUnit.SECONDS);
        backward.get(10, TimeUnit.SECONDS);
        assertEquals(10_000, first.getBalance());
        assertEquals(10_000, second.getBalance());
    }

    @Test
    void concurrentTransfersPreserveTotalAndNonnegativeBalances() throws Exception {
        Account[] accounts = new Account[20];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(100);
        }
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> workers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            workers.add(executor.submit(() -> {
                start.await();
                ThreadLocalRandom random = ThreadLocalRandom.current();
                for (int j = 0; j < 5_000; j++) {
                    Account from = accounts[random.nextInt(accounts.length)];
                    Account to = accounts[random.nextInt(accounts.length)];
                    bank.transfer(from, to, random.nextInt(1, 201));
                    assertTrue(from.getBalance() >= 0);
                    assertTrue(to.getBalance() >= 0);
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> worker : workers) {
            worker.get(10, TimeUnit.SECONDS);
        }
        long total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        assertEquals(2_000, total);
    }
}
