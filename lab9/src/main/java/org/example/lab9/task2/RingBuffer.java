package org.example.lab9.task2;

import java.util.Objects;

public class RingBuffer<T> {
    private static class Node<T> {
        private T value;
        private Node<T> next;
    }

    private Node<T> head;
    private Node<T> tail;

    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Місткість буфера має бути додатною.");
        }

        head = new Node<>();
        Node<T> current = head;
        // Один додатковий вузол розрізняє повний і порожній буфер.
        for (int i = 0; i < capacity; i++) {
            current.next = new Node<>();
            current = current.next;
        }
        current.next = head;
        tail = head;
    }

    public synchronized void put(T value) throws InterruptedException {
        Objects.requireNonNull(value);
        while (tail.next == head) {
            wait();
        }
        tail.value = value;
        tail = tail.next;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (head == tail) {
            wait();
        }
        T value = head.value;
        head.value = null;
        head = head.next;
        notifyAll();
        return value;
    }
}
