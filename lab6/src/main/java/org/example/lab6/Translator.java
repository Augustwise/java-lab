package org.example.lab6;

import java.util.HashMap;

public class Translator {
    private final HashMap<String, String> dictionary = new HashMap<>();

    public void addWord(String english, String ukrainian) {
        dictionary.put(english.toLowerCase(), ukrainian);
    }

    public String translatePhrase(String phrase) {
        String[] words = phrase.split("\\s+");
        StringBuilder translation = new StringBuilder();

        for (String word : words) {
            if (translation.length() > 0) {
                translation.append(' ');
            }
            translation.append(dictionary.getOrDefault(word.toLowerCase(), word));
        }

        return translation.toString();
    }
}
