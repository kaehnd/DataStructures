import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCloud {




    public static void main(String[] args) {
        String filename;
        try (Scanner in = new Scanner(System.in)) {

            System.out.print("Please enter a filename: ");
            filename = in.next();
            in.close();

            HashMap<String, Integer> wordMap = new HashMap<>(10000);

            try (Scanner fileIn = new Scanner(new File(filename))) {
                while (fileIn.hasNext()) {
                    String word = fileIn.next();
                    word = fixWord(word);
                    if (!word.isEmpty()) {
                        int occurrences = wordMap.getOrDefault(word, 0);
                        wordMap.put(word, ++occurrences);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try(PrintWriter writer = new PrintWriter(new File("words.txt"))) {
                wordMap.forEach((s, integer) -> writer.println(integer + " " + s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String fixWord(String s) {
        StringBuilder builder = new StringBuilder(s.toLowerCase());
        for (int i = 0; i < builder.length(); i++) {
            if (!Character.isAlphabetic(builder.charAt(i))) {
                builder.deleteCharAt(i--);
            }
        }

//        Pattern p = Pattern.compile("[^a-z]");
//        Matcher m = new Matcher();
        return builder.toString();
    }




}
