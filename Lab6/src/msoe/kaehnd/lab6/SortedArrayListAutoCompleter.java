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
 * AutoCompleter making use of iterator methods and the SortedArrayList's contains method
 */
public class SortedArrayListAutoCompleter implements AutoCompleter {

    private SortedArrayList<String> wordList;
    private long lastOperationTime;

    /**
     * Constructs a SortedArrayListAutoCompleter by instantiating a new AutoCompleter
     */
    public SortedArrayListAutoCompleter() {
        wordList = new SortedArrayList<>();
    }

    /**
     * Initializes AutoCompleter with a dictionary at the given filename
     *
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
     *
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
     * Returns the time taken to perform the last operation
     *
     * @return long time in nanoseconds
     */
    @Override
    public long getLastOperationTime() {
        return lastOperationTime;
    }

    /**
     * Returns whether the dictionary contains the specified String
     *
     * @param target String to search for
     * @return true if target is found
     */
    @Override
    public boolean contains(String target) {
        return wordList.contains(target.toLowerCase());
    }

    private void checkIllegalState() {
        if (wordList.size() == 0) {
            throw new IllegalStateException("Must call initialize() prior to calling this method");
        }
    }
}
