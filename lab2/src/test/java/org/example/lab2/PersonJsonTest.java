package org.example.lab2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonJsonTest {
    @Test
    void personRemainsEqualAfterJsonConversion() throws Exception {
        Person originalPerson = new Person("Shevchenko", "Anton", 47);
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(originalPerson);
        Person restoredPerson = objectMapper.readValue(json, Person.class);

        assertEquals(originalPerson, restoredPerson);
    }
}
