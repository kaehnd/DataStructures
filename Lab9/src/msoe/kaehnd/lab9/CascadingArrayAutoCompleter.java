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
 *  * AutoCompleter using cascading arrays of Nodes
 *  * to encode locations of words character by character
 *
 *  ****THROWS OutOfMemoryError WHEN USING TOP DOMAINS CSV*******
 */
public class CascadingArrayAutoCompleter implements AutoCompleter {

    private long lastOperationTime;
    private Node root;


    /**
     * Initializes AutoCompleter with a dictionary at the given filename
     *
     * @param filename name of the file
     * @throws IOException when file cannot be loaded
     */
    @Override
    public void initialize(String filename) throws IOException {
        root = null;
        long initialTime = System.nanoTime();
        String extension = filename.substring(filename.lastIndexOf("."));
        try (Scanner in = new Scanner(new File(filename))) {
            if (extension.equals(".txt")) {
                while (in.hasNextLine()) {
                    add(in.nextLine());
                }
            } else if (extension.equals(".csv")) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    add(line.substring(line.indexOf(',') + 1) +
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
        long initialTime = System.nanoTime();
        Node start = findPrefixNode(prefix, prefix, root);
//        List<String> toReturn = new ArrayList<>();
//        if (start != null) {
//            for (Node node : start.array) {
//                if (node != null) {
//                    toReturn.addAll(node.getAllWords());
//                }
//            }
//        }
        List<String> toReturn = start.getAllWords();
        lastOperationTime = System.nanoTime() - initialTime;
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

    private class Node {
        final int numPossibleCharacters = 41;
        private Node[] array;
        private String word;
        private Node() {
            array = new Node[numPossibleCharacters];
        }

        private Node getSubNode(char c) {
            return array[charToIndex(c)];
        }

        private void setSubNode(char c) {
            array[charToIndex(c)] = new Node();
        }

        private void setWord(String word) {
            this.word = word;
        }

        private String getWord() {
            return word;
        }

        private Node[] getArray() {
            return array;
        }

        private List<String> getAllWords() {
            List<String> toReturn = new ArrayList<>();
            for(Node node: array) {
                if (node != null) {
                    if (word != null) {
                        toReturn.add(word);
                    }
                    toReturn.addAll(node.getAllWords());
                }
            }
            return toReturn;
        }

        private int charToIndex(char c) {
            final int valueOfA = 97;
            final int numAlphabetCharacters = 26;
            int result = (int) c - valueOfA;
            if (result >-1 && result < numAlphabetCharacters) {
                return result;
            }
            switch (c) {
                case '-':
                    return numAlphabetCharacters;
                case '.':
                    return numAlphabetCharacters + 1;
                case '0':
                    return numAlphabetCharacters + 2;
                case '1':
                    return numAlphabetCharacters + 3;
                case '2':
                    return numAlphabetCharacters + 4;
                case '3':
                    return numAlphabetCharacters + 5;
                case '4':
                    return numAlphabetCharacters + 6;
                case '5':
                    return numAlphabetCharacters + 7;
                case '6':
                    return numAlphabetCharacters + 8;
                case '7':
                    return numAlphabetCharacters + 9;
                case '8':
                    return numAlphabetCharacters + 10;
                case '9':
                    return numAlphabetCharacters + 11;
                case '\'':
                    return numAlphabetCharacters + 12;
                case ' ':
                    return numAlphabetCharacters + 13;
                case ':':
                    return numAlphabetCharacters + 14;
                default:
                    return -1;
            }
        }
    }

    private void add(String word) {
        if (root == null) {
            root = new Node();
        }
        add(word, word, root);
    }

    private void add(String partialWord, String word, Node current) {
        char first = partialWord.charAt(0);
        if (partialWord.length() == 1) {
            if (current.getSubNode(first) == null) {
                current.setSubNode(first);
            }
            current.getSubNode(first).setWord(word);
        } else {
            if (current.getSubNode(first) == null) {
                current.setSubNode(first);
            }
            add(partialWord.substring(1), word, current.getSubNode(first));
        }
    }

    private Node findPrefixNode(String partialPrefix, String prefix, Node current) {

        char first = partialPrefix.charAt(0);
        if (current.getWord() != null && current.getWord().startsWith(prefix)||
                partialPrefix.length() == 1) {
            return current.getSubNode(first);
        }
        if (current.getSubNode(first) == null) {
            return null;
        }
        return findPrefixNode(partialPrefix.substring(1), prefix, current.getSubNode(first));
    }
}
