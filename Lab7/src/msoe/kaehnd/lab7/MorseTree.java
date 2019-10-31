/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 7: MorseDecoder
 * Name: Daniel Kaehn
 * Created: 4/17/2019
 */
package msoe.kaehnd.lab7;

/**
 * Builds a binary tree with right nodes representing dots and left nodes representing dashes
 * to specify tree location of all morse encoded symbols
 * @param <E> Symbol type stored in the tree
 */
public class MorseTree<E> {

    private Node head;

    private class Node {

        private Node right;
        private Node left;
        private E data;

        private Node() {
            this.data = null;
            this.left = null;
            this.right = null;
        }

        private Node getRight() {
            return this.right;
        }

        private Node getLeft() {
            return this.left;
        }

        private void setRight(Node right) {
            this.right = right;
        }

        private void setLeft(Node left) {
            this.left = left;
        }

        private E getData() {
            return this.data;
        }

        private void setData(E data) {
            this.data = data;
        }
    }

    /**
     * Constructs a MorseTree, instantiating the root node
     */
    public MorseTree() {
        this.head = new Node();
    }

    /**
     * Adds the specified symbol to the MorseTree at the location specified by the code
     * @param symbol symbol to be stored
     * @param code morse code String specifying the location of the symbol
     * @throws IllegalArgumentException if the code contains non '-' or '.' chars
     */
    public void add(E symbol, String code) throws IllegalArgumentException{

        Node currentNode = head;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '-') {
                if (currentNode.getRight() == null) {
                    currentNode.setRight(new Node());
                }
                currentNode = currentNode.getRight();
            } else if (code.charAt(i) == '.') {
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(new Node());
                }
                currentNode = currentNode.getLeft();
            } else if (code.charAt(i) != ' '){
                throw new IllegalArgumentException(code.charAt(i) + "is not a legal morse code" +
                        " character, skipping its encoding");
            }
        }
        currentNode.setData(symbol);
    }

    /**
     * Makes use of the recursiveDecode() method to find the specified code location
     * and return the symbol stored there
     * @param code Morse Code String
     * @return symbol of generic type stored at the location specified by the code
     * @throws IllegalArgumentException if the code contains a non '-' or '.' char
     */
    public E decode(String code) throws IllegalArgumentException{
        return recursiveDecode(code, head);
    }

    private E recursiveDecode(String code, Node current) throws IllegalArgumentException{
        if (code.isEmpty()) {
            return current.getData();
        }
        if (code.charAt(0) == '-') {
            return recursiveDecode(code.substring(1), current.getRight());
        }
        if (code.charAt(0) == '.') {
            return recursiveDecode(code.substring(1), current.getLeft());
        }
        throw new IllegalArgumentException("Warning: skipping code with illegal character \""
                + code.charAt(0)+"\"");
    }
}
