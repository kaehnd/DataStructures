/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 6: WordSearch
 * Name: Daniel Kaehn
 * Created: 4/14/2019
 */
package msoe.kaehnd.lab6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AutoCompleter making use of iterator methods to implement allThatBeginWith()
 */
public class IteratorAutoCompleter implements AutoCompleter {

    private List<String> wordList;
    private long lastOperationTime;


    /**
     * Constructs an IteratorAutoCompleter with an empty List<String>
     * @param emptyList empty List<String>
     */
    public IteratorAutoCompleter(List<String> emptyList) {
        wordList = emptyList;
    }

    /**
     * Initializes AutoCompleter with a dictionary at the given filename
     * @param filename name of the file
     * @throws IOException when file cannot be loaded
     */
    @Override
    public void initialize(String filename) throws IOException {
        long initialTime = System.nanoTime();
        String extension = filename.substring(filename.lastIndexOf("."));
        try (Scanner in = new Scanner(new File(filename))) {
            if (extension.equals(".txt")) {
                while (in.hasNextLine()) {
                    wordList.add(in.nextLine());
                }
            } else if (extension.equals(".csv")) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    wordList.add(line.substring(line.indexOf(',') + 1) +
                            ": " + line.substring(0, line.indexOf(',')));
                }
            } else {
                throw new IOException("Invalid Filetype");
            }
        }
        lastOperationTime = System.nanoTime() - initialTime;
    }

    /**
     * Returns a List<String> of every String in dictionary List<String> beginning with the prefix
     * using a foreach loop (Iterator)
     * @param prefix String to check with every List item
     * @return a List<String> including every String beginning with prefix
     */
    @Override
    public List<String> allThatBeginWith(String prefix) {
        checkIllegalState();
        long initalTime = System.nanoTime();
        ArrayList<String> temp = new ArrayList<>();
        for(String s: wordList) {
            if (s.toLowerCase().startsWith(prefix.toLowerCase())) {
                temp.add(s);
            }
        }
        lastOperationTime = System.nanoTime() - initalTime;
        return temp;
    }

    /**
     * Checks if initialize() has been called, then returns time of last operation
     * @return time in nanoseconds of last operation
     */
    @Override
    public long getLastOperationTime() {
        checkIllegalState();
        return lastOperationTime;
    }


    /**
     * Returns whether the dictionary contains the specified String
     *
     * @param target String to search for
     * @return true if target is found
     * @throws IllegalStateException when called before initialize()
     */
    @Override
    public boolean contains(String target) throws IllegalStateException{
        checkIllegalState();
        for (String s: wordList) {
            if (s.toLowerCase().equals(target.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void checkIllegalState() {
        if (wordList.size() == 0) {
            throw new IllegalStateException("Must call initialize() prior to calling this method");
        }
    }
}
