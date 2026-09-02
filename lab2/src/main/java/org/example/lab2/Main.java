package org.example.lab2;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        Person originalPerson = new Person("Shevchenko", "Anton", 47);

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(originalPerson);
        Person restoredPerson = objectMapper.readValue(json, Person.class);

        System.out.println("Original object: " + originalPerson);
        System.out.println("JSON: " + json);
        System.out.println("Restored object: " + restoredPerson);
        System.out.println("Objects are equal: " + originalPerson.equals(restoredPerson));
    }
}
