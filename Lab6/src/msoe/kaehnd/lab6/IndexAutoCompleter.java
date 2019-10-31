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
 * AutoCompleter making use of indexed methods to implement allThatBeginWith()
 */
public class IndexAutoCompleter implements AutoCompleter {

    private List<String> wordList;

    private long lastOperationTime;


    /**
     * Constructs an IndexAutoCompleter with a List<String> of any type
     * @param emptyList empty List<String>
     */
    public IndexAutoCompleter(List<String> emptyList) {
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
     * using indexed methods
     * @param prefix String to check with every List item
     * @return a List<String> including every String beginning with prefix
     */
    @Override
    public List<String> allThatBeginWith(String prefix) {
        checkIllegalState();
        long initialTime = System.nanoTime();
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            String current = wordList.get(i);
            if (current.toLowerCase().startsWith(prefix.toLowerCase())) {
                temp.add(current);
            }
        }
        lastOperationTime = System.nanoTime() - initialTime;
        return temp;
    }

    /**
     * Checks if initialize has been called, then gets tinme for last operation
     * @return time in nanoseconds of the last operation
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
     */
    @Override
    public boolean contains(String target) {
        checkIllegalState();
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).toLowerCase().equals(target.toLowerCase())){
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
