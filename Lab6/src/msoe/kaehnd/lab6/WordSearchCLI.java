/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 6: WordSearch
 * Name: Daniel Kaehn
 * Created: 4/14/2019
 */
package msoe.kaehnd.lab6;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interfaces the GameBoard object with the Command Line for program usage
 */
public class WordSearchCLI {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Please launch program with three arguments:\n" +
                    "1) filename of grid text file\n2) filename of dictionary text file\n" +
                    "3) name of strategy to be implemented");
            return;
        }
        String gridName = args[0];
        String fileName = args[1];
        String strategy = args[2];

        int indexOfI = strategy.indexOf('I');

        if (indexOfI == -1) {
            if (!strategy.equals("SortedArrayList")) {
                System.err.println("Strategy argument malformed, exiting program");
                return;
            }
        }
        String dataStructure;
        String completerType;
        AutoCompleter completer;
        if (!strategy.equals("SortedArrayList")) {
            dataStructure = strategy.substring(0, indexOfI);
            completerType = strategy.substring(indexOfI);


            List<String> stringList;

            switch (dataStructure) {
                case "ArrayList":
                    stringList = new ArrayList<>();
                    break;
                case "LinkedList":
                    stringList = new LinkedList<>();
                    break;
                case "SortedArrayList":
                    stringList = new SortedArrayList<>();
                    break;
                default:
                    System.err.println("Strategy argument malformed, exiting program");
                    return;
            }

            switch (completerType) {
                case "Indexed":
                    completer = new IndexAutoCompleter(stringList);
                    break;
                case "Iterated":
                    completer = new IteratorAutoCompleter(stringList);
                    break;
                default:
                    System.err.println("Strategy argument malformed, exiting program");
                    return;
            }
        } else {
            completer = new SortedArrayListAutoCompleter();
        }

        try {
            completer.initialize(fileName);
            GameBoard board = new GameBoard(completer);
            board.load(Paths.get(gridName));

            long startTime = System.nanoTime();

            List<String> foundWords = board.findWords();

            String runTime = timeToString(System.nanoTime() - startTime);

            for (String s : foundWords) {
                System.out.println(s);
            }

            System.out.println("Search found " + foundWords.size() + " words");
            System.out.println("Search runtime: " + runTime);
        } catch (IOException e) {
            System.err.println("File could not be loaded");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String timeToString(long nanos) {
        final double nanToSec = 0.000000001;
        final double nanToMillis = 0.000001;
        final double nanToMicro = 0.001;
        if (TimeUnit.NANOSECONDS.toSeconds(nanos) > 0) {
            long min = TimeUnit.NANOSECONDS.toMinutes(nanos);
            nanos -= TimeUnit.MINUTES.toNanos(min);
            double sec = nanos * nanToSec;
            return String.format("%02d:%07.4f", min, sec);
        } else if (TimeUnit.NANOSECONDS.toMillis(nanos) > 0) {
            double millis = nanos * nanToMillis;
            return String.format("%.4f ms", millis);
        } else if (TimeUnit.NANOSECONDS.toMicros(nanos) > 0) {
            double micros = nanos * nanToMicro;
            return String.format("%.4f \u03BCs", micros);
        } else {
            return nanos + " ns";
        }
    }
}
