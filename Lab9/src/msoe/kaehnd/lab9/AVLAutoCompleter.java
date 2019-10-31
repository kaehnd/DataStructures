/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 9: AutoCompleter Revisited
 * Name: Daniel Kaehn
 * Created: 5/12/2019
 */
package msoe.kaehnd.lab9;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AutoCompleter making use of AVLTree<E> and Node<E> to store and find words
 */
public class AVLAutoCompleter implements AutoCompleter {

    private long lastOperationTime;
    AVLTree<String> wordTree = new AVLTree<>();

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
                    wordTree.add(in.nextLine());
                }
            } else if (extension.equals(".csv")) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    wordTree.add(line.substring(line.indexOf(',') + 1) +
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
        long intialTime = System.nanoTime();
        Node<String> start = findPrefixNode(prefix, wordTree.getRoot());
        List<String> toReturn = allThatBeginWith(prefix, start);
        lastOperationTime = System.nanoTime() - intialTime;
        return toReturn;
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

    //Searches for highest node starting with the passed prefix and returns it
    private Node<String> findPrefixNode(String prefix, Node<String> current) {
        int comparison = prefix.compareTo(current.getValue());
        if (current.getValue().startsWith(prefix) || comparison == 0) {
            return current;
        } else if (comparison > 0) {
            if (current.getRight() == null) {
                return null;
            }
            return findPrefixNode(prefix, current.getRight());
        } else {
            if (current.getLeft() == null) {
                return null;
            }
            return findPrefixNode(prefix, current.getLeft());
        }
    }

    //Iterates through all of the children of the passed node and returns a
    // List<String> of all that begin with the prefix
    private List<String> allThatBeginWith(String prefix, Node<String> current) {
        ArrayList<String> words = new ArrayList<>();
        if (current != null) {
            if (current.getValue().startsWith(prefix)) {
                words.add(current.getValue());
            }
            words.addAll(allThatBeginWith(prefix, current.getLeft()));
            words.addAll(allThatBeginWith(prefix, current.getRight()));
        }
        return words;
    }
}
