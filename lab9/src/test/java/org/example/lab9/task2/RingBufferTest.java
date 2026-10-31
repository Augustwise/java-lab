package org.example.lab9.task2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferTest {
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
    void preservesOrderWhenPointersGoAroundTheRing() throws InterruptedException {
        RingBuffer<Integer> buffer = new RingBuffer<>(2);
        for (int i = 0; i < 100; i++) {
            buffer.put(i);
            buffer.put(i + 1);
            assertEquals(i, buffer.take());
            assertEquals(i + 1, buffer.take());
        }
    }

    @Test
    void rejectsInvalidCapacityAndNull() {
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<>(0));
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<>(-1));
        RingBuffer<String> buffer = new RingBuffer<>(1);
        assertThrows(NullPointerException.class, () -> buffer.put(null));
    }

    @Test
    void waitsForDataInEmptyBuffer() throws Exception {
        RingBuffer<String> buffer = new RingBuffer<>(1);
        CountDownLatch started = new CountDownLatch(1);
        Future<String> reader = executor.submit(() -> {
            started.countDown();
            return buffer.take();
        });
        assertTrue(started.await(5, TimeUnit.SECONDS));
        assertThrows(TimeoutException.class, () -> reader.get(100, TimeUnit.MILLISECONDS));
        buffer.put("message");
        assertEquals("message", reader.get(5, TimeUnit.SECONDS));
    }

    @Test
    void waitsForSpaceInFullBuffer() throws Exception {
        RingBuffer<Integer> buffer = new RingBuffer<>(1);
        buffer.put(1);
        CountDownLatch started = new CountDownLatch(1);
        Future<?> writer = executor.submit(() -> {
            started.countDown();
            buffer.put(2);
            return null;
        });
        assertTrue(started.await(5, TimeUnit.SECONDS));
        assertThrows(TimeoutException.class, () -> writer.get(100, TimeUnit.MILLISECONDS));
        assertEquals(1, buffer.take());
        writer.get(5, TimeUnit.SECONDS);
        assertEquals(2, buffer.take());
    }

    @Test
    void multipleProducersAndConsumersDoNotLoseOrDuplicateData() throws Exception {
        RingBuffer<Integer> buffer = new RingBuffer<>(3);
        List<Future<?>> producers = new ArrayList<>();
        List<Future<List<Integer>>> consumers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final int producer = i;
            producers.add(executor.submit(() -> {
                for (int j = 0; j < 500; j++) {
                    buffer.put(producer * 500 + j);
                }
                return null;
            }));
        }
        for (int i = 0; i < 2; i++) {
            consumers.add(executor.submit(() -> {
                List<Integer> values = new ArrayList<>();
                for (int j = 0; j < 1_000; j++) {
                    values.add(buffer.take());
                }
                return values;
            }));
        }
        for (Future<?> producer : producers) {
            producer.get(10, TimeUnit.SECONDS);
        }
        Set<Integer> received = new HashSet<>();
        for (Future<List<Integer>> consumer : consumers) {
            for (int value : consumer.get(10, TimeUnit.SECONDS)) {
                assertTrue(received.add(value), "Повторне отримання: " + value);
            }
        }
        assertEquals(2_000, received.size());
        for (int i = 0; i < 2_000; i++) {
            assertTrue(received.contains(i), "Втрачено значення: " + i);
        }
    }
}
