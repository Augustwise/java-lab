package org.example.lab4;

public abstract class Automobile<P extends Person> extends Vehicle<P> {
    protected Automobile(int capacity) {
        super(capacity);
    }
}
