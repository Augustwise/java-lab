package org.example.lab10.task3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MessagesTest {
    private static ResourceBundle bundle(Locale locale) {
        return ResourceBundle.getBundle(Messages.BASE_NAME, locale,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    @Test
    void everyLanguageHasTheSameKeysAsTheBaseFile() {
        Set<String> expected = Collections.list(bundle(Messages.UKRAINIAN).getKeys()).stream()
                .collect(java.util.stream.Collectors.toSet());

        for (Locale locale : Messages.SUPPORTED) {
            Set<String> actual = Collections.list(bundle(locale).getKeys()).stream()
                    .collect(java.util.stream.Collectors.toSet());
            assertEquals(expected, actual, "Ключі не збігаються для мови " + locale);
        }
    }

    @Test
    void languageIsChosenRegardlessOfDefaultLocale() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ENGLISH);
            assertEquals("Українська", new Messages(Messages.UKRAINIAN).get("language.name"));
            assertEquals("English", new Messages(Messages.ENGLISH).get("language.name"));
            assertEquals("Deutsch", new Messages(Messages.GERMAN).get("language.name"));
        } finally {
            Locale.setDefault(previous);
        }
    }

    @Test
    void switchingLocaleChangesTexts() {
        Messages messages = new Messages(Messages.UKRAINIAN);
        String ukrainian = messages.get("menu.exit");

        messages.setLocale(Messages.ENGLISH);

        assertEquals("Exit", messages.get("menu.exit"));
        assertNotEquals(ukrainian, messages.get("menu.exit"));
    }

    @Test
    void formatSubstitutesValuesAndKeepsApostrophes() {
        Messages messages = new Messages(Messages.UKRAINIAN);

        assertEquals("Результат записано у файл: out.txt", messages.format("result.saved", "out.txt"));
        assertTrue(messages.get("source.prompt").contains("ім'я"));
    }

    @Test
    void findsLanguageByNumberOrCode() {
        assertEquals(Messages.UKRAINIAN, Messages.findSupported("1"));
        assertEquals(Messages.ENGLISH, Messages.findSupported(" EN "));
        assertEquals(Messages.GERMAN, Messages.findSupported("de"));
        assertNull(Messages.findSupported("fr"));
        assertNull(Messages.findSupported("9"));
    }
}
