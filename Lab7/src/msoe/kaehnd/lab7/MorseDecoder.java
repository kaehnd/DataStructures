/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 7: MorseDecoder
 * Name: Daniel Kaehn
 * Created: 4/17/2019
 */
package msoe.kaehnd.lab7;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Makes use of the MorseTree class to build a binary tree from a morse code translating file,
 *  decode a specified morse code encoded file, and store a specified decoded text file
 */
public class MorseDecoder {

    public static void main(String[] args) {

        try (Scanner in = new Scanner(System.in)) {
            MorseTree<String> morseTree = loadDecoder(Paths.get("morsecode.txt"));

            System.out.print("Enter an input filename: ");
            String input = in.nextLine();

            System.out.print("Enter an output filename: ");
            String output = in.nextLine();

            decodeToFile(Paths.get(input), Paths.get(output), morseTree);
            System.out.println("File conversion successful!");
        } catch (IOException e) {
            System.out.println("File loading fail");
        }
    }

    private static MorseTree<String> loadDecoder(Path path) throws IOException {
        MorseTree<String> morseTree = new MorseTree<>();
        try (Scanner in = new Scanner(path.toFile())) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String symbol;
                if (line.charAt(0) == '\\') {
                    symbol = "\n";
                    line = line.substring(2);
                } else {
                    symbol = line.substring(0, 1);
                    line = line.substring(1);
                }
                morseTree.add(symbol, line);
            }
        }
        return morseTree;
    }

    private static void decodeToFile(Path inputFile, Path outputFile,
                                     MorseTree<String> morseTree) throws IOException {
        try (Scanner in = new Scanner(inputFile.toFile());
                PrintWriter out = new PrintWriter(outputFile.toFile())) {
            while (in.hasNext()) {
                try {
                    String symbol = morseTree.decode(in.next());
                    out.print(symbol);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}