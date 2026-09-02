package org.example.lab2;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Mode;
import org.junit.jupiter.api.Test;

class PersonEqualsTest {
    @Test
    void equalsAndHashCode() {
        EqualsVerifier.forClass(Person.class)
                .set(Mode.finalMeansFinal())
                .verify();
    }
}
