import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static String[] findWords(String text) {
        List<String> result = new ArrayList<>();

        for (String word : text.split("\\s+")) {
            if (!word.matches("[A-Za-z]+")) {
                continue;
            }

            int vowels = 0;
            for (char letter : word.toLowerCase().toCharArray()) {
                if ("aeiou".indexOf(letter) >= 0) {
                    vowels++;
                }
            }

            if (vowels == word.length() - vowels) {
                result.add(word);
            }
        }

        return result.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        System.out.println(Arrays.toString(findWords(text)));
    }
}
