package org.example.lab2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Person {
    private final String lastName;
    private final String firstName;
    private final int age;

    @JsonCreator
    public Person(
            @JsonProperty("lastName") String lastName,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("age") int age) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Person person = (Person) object;
        return age == person.age
                && Objects.equals(lastName, person.lastName)
                && Objects.equals(firstName, person.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }
}
