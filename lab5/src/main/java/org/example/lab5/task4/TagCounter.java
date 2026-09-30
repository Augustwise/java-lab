package org.example.lab5.task4;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TagCounter {
    public Map<String, Integer> count(String url) throws IOException {
        Map<String, Integer> counts = new HashMap<>();
        URLConnection connection = URI.create(url).toURL().openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            new ParserDelegator().parse(reader, new HTMLEditorKit.ParserCallback() {
                @Override
                public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
                    counts.merge(tag.toString(), 1, Integer::sum);
                }

                @Override
                public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
                    counts.merge(tag.toString(), 1, Integer::sum);
                }
            }, true);
        }
        return counts;
    }
}
