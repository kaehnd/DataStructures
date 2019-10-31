/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 9: AutoCompleterRevisited
 * Name: Daniel Kaehn
 * Created: 5/12/2019
 */
package msoe.kaehnd.lab9;

/**
 * Node class used by AVLTree separated for use in AVLAutoCompleter
 * @param <E> Generic type of value stored in Node and Tree
 */
public class Node<E extends Comparable<?super E>> {
    private Node<E> left;
    private Node<E> right;
    private E value;
    private int height = 1;

    /**
     * Constructs a Node
     * @param value value of generic type E
     */
    public Node(E value) {
        this.value = value;
    }

    public Node<E> getLeft() {
        return left;
    }

    public Node<E> getRight() {
        return right;
    }

    public int getHeight() {
        return height;
    }

    public E getValue() {
        return value;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

