package org.example.lab9.task2;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final int BUFFER_CAPACITY = 10;
    private static final int MESSAGE_COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        RingBuffer<String> firstBuffer = new RingBuffer<>(BUFFER_CAPACITY);
        RingBuffer<String> secondBuffer = new RingBuffer<>(BUFFER_CAPACITY);

        for (int i = 1; i <= 5; i++) {
            final int number = i;
            Thread producer = new Thread(() -> {
                try {
                    int messageNumber = 1;
                    while (!Thread.currentThread().isInterrupted()) {
                        firstBuffer.put("Потік № " + number
                                + " згенерував повідомлення " + messageNumber++);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + number);
            producer.setDaemon(true);
            producer.start();
        }

        for (int i = 6; i <= 7; i++) {
            final int number = i;
            Thread translator = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        String message = firstBuffer.take();
                        secondBuffer.put("Потік № " + number
                                + " переклав повідомлення «" + message + "»");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Translator-" + number);
            translator.setDaemon(true);
            translator.start();
        }

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            System.out.println(secondBuffer.take());
        }
    }
}
