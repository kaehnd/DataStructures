/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 8: MorseEncoder
 * Name: Daniel Kaehn
 * Created: 5/1/2019
 */
package msoe.kaehnd.lab8;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Makes use of the LookupTable to encode text into Morse Code
 */
public class MorseEncoder {

    public static void main(String[] args) {
        try(Scanner in = new Scanner(System.in)) {

            System.out.print("Enter an input filename: ");
            String inputPath = in.next();
            System.out.print("Enter an output filename: ");
            String outputPath = in.next();

            LookupTable<Character, String> table = loadTable(Paths.get("morsecode.txt"));
            encodeToFile(table, Paths.get(inputPath), Paths.get(outputPath));
        } catch (IOException e) {
            System.out.println("Error loading file");
        }
    }

    //Create a LookupTable and load Morse Code mappings from file
    private static LookupTable<Character, String> loadTable(Path path) throws IOException {
        LookupTable<Character, String> table = new LookupTable<>();
        try (Scanner in = new Scanner(path)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                int i = 0;
                char key = line.charAt(i++);

                //Handles newline special case
                if (key == '\\') {
                    key = '\n';
                    i++;
                }
                int j = i;

                //Handles spaces put after Morse Code in file
                while (j < line.length() && line.charAt(j) != ' ') {
                    j++;
                }
                String value = line.substring(i, j);
                table.put(key, value);
            }
        }
        return table;
    }

    //Encodes text file with Morse Code from non-encoded text file using passed LookupTable
    private static void encodeToFile(LookupTable<Character, String> table,
                                     Path src, Path dst) throws IOException{
        try (Scanner in = new Scanner(src);
                PrintWriter printWriter = new PrintWriter(dst.toFile())) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    String value = table.get(Character.toUpperCase(line.charAt(i)));
                    if (value == null) {
                        System.out.println("Warning: skipping illegal character \"" +
                                line.charAt(i) + "\"");
                    } else {
                        printWriter.print(value + " ");
                    }
                } //Encodes newline after each line break
                printWriter.println(table.get('\n'));
            }
        }
    }
}
