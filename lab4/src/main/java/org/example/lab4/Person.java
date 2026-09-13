package org.example.lab4;

import java.util.Objects;

public class Person {
    private final String name;

    public Person(String name) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + name + '}'; // Example: Firefighter{Ihor}
    }
}
