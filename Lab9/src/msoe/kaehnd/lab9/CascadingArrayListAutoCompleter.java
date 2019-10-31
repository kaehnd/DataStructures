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
 * AutoCompleter using cascading ArrayLists of Nodes
 * to encode locations of words character by character
 */
public class CascadingArrayListAutoCompleter implements AutoCompleter {

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
        List<String> toReturn = new ArrayList<>();
        if (start != null) {
            for (Node node : start.array) {
                if (node != null) {
                    toReturn.addAll(node.getAllWords());
                }
            }
        }
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
        private ArrayList<Node> array;
        private String word;
        private char key;
        private Node(char key) {
            array = new ArrayList<>();
            this.key = key;
        }

        private char getKey() {
            return key;
        }

        private void setWord(String word) {
            this.word = word;
        }

        private String getWord() {
            return word;
        }

        private void addSubnode(char c) {
            array.add(new Node(c));
        }

        private Node findSubnode(char c) {
            for(Node node: array) {
                if (node.getKey() == c) {
                    return node;
                }
            }
            return null;
        }

        private List<String> getAllWords() {
            List<String> toReturn = new ArrayList<>();
            for(Node node: array) {
                if (node.getWord() != null) {
                    toReturn.add(node.getWord());
                }
                toReturn.addAll(node.getAllWords());
            }
            return toReturn;
        }
    }

    private void add(String word) {
        if (root == null) {
            root = new Node('*');
        }
        add(word, word, root);
    }

    private void add(String partialWord, String word, Node current) {
        char first = partialWord.charAt(0);
        if (partialWord.length() == 1) {
            if (current.findSubnode(first) == null) {
                current.addSubnode(first);
            }
            current.findSubnode(first).setWord(word);
        } else {
            if (current.findSubnode(first) == null) {
                current.addSubnode(first);
            }
            add(partialWord.substring(1), word, current.findSubnode(first));
        }
    }

    //Recurses down ArrayLists to find the path where all words starting with the prefix will lie
    private Node findPrefixNode(String partialPrefix, String prefix, Node current) {
        char first = partialPrefix.charAt(0);
        Node subNode = current.findSubnode(first);
        if (current.getWord() != null && current.getWord().startsWith(prefix)||
                partialPrefix.length() == 1) {
            return current;
        }
        if (subNode == null) {
            return null;
        }
        return findPrefixNode(partialPrefix.substring(1), prefix, subNode);
    }
}
