package org.example.lab10.task1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StringMutatorTest {
    @Test
    void replacesContentOfSameObject() {
        String text = new String("Hello");
        String alias = text;

        StringMutator.replaceContent(text, "Goodbye, world");

        assertEquals("Goodbye, world", text);
        assertEquals(14, text.length());
        assertSame(text, alias);
    }

    @Test
    void switchesBetweenLatin1AndUtf16() {
        String text = new String("abc");

        StringMutator.replaceContent(text, "Привіт");
        assertEquals("Привіт", text);
        assertEquals(6, text.length());
        assertTrue(StringMutator.describeInternals(text).contains("UTF16"));

        StringMutator.replaceContent(text, "xyz");
        assertEquals("xyz", text);
        assertTrue(StringMutator.describeInternals(text).contains("LATIN1"));
    }

    @Test
    void resetsCachedHashCode() {
        String text = new String("first");
        int oldHash = text.hashCode();

        StringMutator.replaceContent(text, "second");

        assertEquals("second".hashCode(), text.hashCode());
        assertTrue(oldHash != text.hashCode());
        Map<String, Integer> map = new HashMap<>();
        map.put("second", 1);
        assertEquals(1, map.get(text));
    }

    @Test
    void replacementStaysIndependentOfTarget() {
        String replacement = new String("value");
        String text = new String("other");

        StringMutator.replaceContent(text, replacement);
        StringMutator.replaceContent(replacement, "changed");

        assertEquals("value", text);
        assertEquals("changed", replacement);
    }

    @Test
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> StringMutator.replaceContent(null, "x"));
        assertThrows(NullPointerException.class, () -> StringMutator.replaceContent(new String("x"), null));
    }
}
