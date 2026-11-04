package org.example.lab10.task1;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Objects;

public final class StringMutator {
    private StringMutator() {
    }

    public static void replaceContent(String target, String replacement) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(replacement, "replacement");

        try {
            Field value = field("value");
            // У Java 8 символи зберігалися в char[], а з Java 9 — у byte[] разом із полем coder (0 — Latin-1, 1 — UTF-16)
            if (value.getType() == char[].class) {
                char[] chars = ((char[]) value.get(replacement)).clone();
                value.set(target, chars);
            } else {
                Field coder = field("coder");
                byte[] bytes = ((byte[]) value.get(replacement)).clone();
                byte replacementCoder = coder.getByte(replacement);
                value.set(target, bytes);
                coder.setByte(target, replacementCoder);
            }
            resetCachedHash(target);
        } catch (InaccessibleObjectException e) {
            throw new IllegalStateException(
                    "Пакет java.lang закритий. Запустіть JVM з --add-opens=java.base/java.lang=ALL-UNNAMED", e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Не вдалося змінити внутрішній стан рядка: " + e, e);
        }
    }

    public static String describeInternals(String text) {
        try {
            Field value = field("value");
            Object array = value.get(text);
            if (array instanceof char[] chars) {
                return "char[" + chars.length + "]";
            }
            byte coder = field("coder").getByte(text);
            return "byte[" + ((byte[]) array).length + "], coder=" + coder + (coder == 0 ? " (LATIN1)" : " (UTF16)");
        } catch (InaccessibleObjectException | ReflectiveOperationException e) {
            return "недоступно: " + e.getClass().getSimpleName();
        }
    }

    private static Field field(String name) throws NoSuchFieldException {
        Field field = String.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    // Скидаємо закешований hashCode, щоб після зміни тексту він перерахувався заново
    private static void resetCachedHash(String target) throws ReflectiveOperationException {
        Field hash = field("hash");
        hash.setInt(target, 0);
        try {
            field("hashIsZero").setBoolean(target, false);
        } catch (NoSuchFieldException ignored) {
        }
    }
}
