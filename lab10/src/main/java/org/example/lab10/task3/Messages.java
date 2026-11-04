package org.example.lab10.task3;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Messages {
    public static final String BASE_NAME = "location.messages";
    public static final Locale UKRAINIAN = Locale.forLanguageTag("uk");
    public static final Locale ENGLISH = Locale.forLanguageTag("en");
    public static final Locale GERMAN = Locale.forLanguageTag("de");
    public static final List<Locale> SUPPORTED = List.of(UKRAINIAN, ENGLISH, GERMAN);

    private Locale locale;
    private ResourceBundle bundle;

    public Messages(Locale locale) {
        setLocale(locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.bundle = load(locale);
        this.locale = locale;
    }

    public String get(String key) {
        return bundle.getString(key);
    }

    public String format(String key, Object... args) {
        return String.format(locale, get(key), args);
    }

    public static String nativeName(Locale locale) {
        return load(locale).getString("language.name");
    }

    // Дозволяє обрати мову за номером у списку (1, 2, 3) або за кодом (uk, en, de)
    public static Locale findSupported(String input) {
        String text = input.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < SUPPORTED.size(); i++) {
            if (text.equals(String.valueOf(i + 1)) || text.equals(SUPPORTED.get(i).getLanguage())) {
                return SUPPORTED.get(i);
            }
        }
        return null;
    }

    // Вимикаємо підстановку системної мови ОС, щоб для української завжди завантажувався базовий messages.properties
    private static ResourceBundle load(Locale locale) {
        return ResourceBundle.getBundle(BASE_NAME, locale,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
}
