package org.example.lab8;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelMonteCarloPi {
    private static final long ITERATIONS = 1_000_000_000L;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Вкажіть кількість потоків одним аргументом.");
        }

        int threadCount = Integer.parseInt(args[0]);
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Кількість потоків має бути додатною.");
        }

        Thread[] threads = new Thread[threadCount];
        long[] insideCounts = new long[threadCount];
        long start = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            final long points = ITERATIONS / threadCount + (i < ITERATIONS % threadCount ? 1 : 0);

            threads[i] = new Thread(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                long inside = 0;

                for (long j = 0; j < points; j++) {
                    double x = random.nextDouble();
                    double y = random.nextDouble();
                    if (x * x + y * y <= 1.0) {
                        inside++;
                    }
                }

                insideCounts[index] = inside;
            });
            threads[i].start();
        }

        long totalInside = 0;
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            totalInside += insideCounts[i];
        }

        double elapsedMs = (System.nanoTime() - start) / 1_000_000.0;
        double pi = 4.0 * totalInside / ITERATIONS;

        System.out.printf(Locale.US, "PI is %.5f%n", pi);
        System.out.println("THREADS " + threadCount);
        System.out.printf(Locale.US, "ITERATIONS %,d%n", ITERATIONS);
        System.out.printf(Locale.US, "TIME %.2fms%n", elapsedMs);
    }
}
