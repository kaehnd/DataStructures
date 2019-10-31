/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 9: AutoCompleterRevisited
 * Name: Daniel Kaehn
 * Created: 5/12/2019
 */
package msoe.kaehnd.lab9;

/**
 * Partially Implemented BinarySearchTree with balancing add() method
 * @param <E> Comparable type stored in the AVLTree
 */
public class AVLTree<E extends Comparable<?super E>> {

    private Node<E> root;

    /**
     * Default constructor
     */
    public AVLTree() {

    }

    public Node<E> getRoot() {
        return root;
    }

    /**
     * Makes use of the recursive add() method to add an element and balance the tree
     * @param value value of type E to be added
     */
    public void add(E value) {
        if (root == null) {
            root = new Node<>(value);
        } else {
            root = add(root, value);
        }
    }


    private Node<E> add(Node<E> current, E value) {
        int comparison = (value.compareTo(current.getValue()));
        if (comparison < 0) {
            if (current.getLeft() == null) {
                current.setLeft(new Node<>(value));
            } else {
                current.setLeft(add(current.getLeft(), value));
            }
        } else {
            if (current.getRight() == null) {
                current.setRight(new Node<>(value));
            } else {
                current.setRight(add(current.getRight(), value));
            }
        }
        int leftHeight = height(current.getLeft());
        int rightHeight = height(current.getRight());

        //update height
        current.setHeight(Math.max(leftHeight, rightHeight) + 1);

        //Compute values used to determine state of imbalance
        int balance = rightHeight - leftHeight;
        int compareLeft = current.getLeft() == null ? 0 :
                value.compareTo(current.getLeft().getValue());
        int compareRight = current.getRight() == null ? 0 :
                value.compareTo(current.getRight().getValue());

        //right right
        if (balance > 1 && compareRight > 0) {
            return rotateLeft(current);
        }

        //right left
        if (balance > 1 && compareRight < 0) {
            current.setRight(rotateRight(current.getRight()));
            return rotateLeft(current);
        }

        //left left
        if (balance < -1 && compareLeft < 0) {
            return rotateRight(current);
        }

        //left right
        if (balance < -1 && compareLeft > 0) {
            current.setLeft(rotateLeft(current.getLeft()));
            return rotateRight(current);
        }
        return current;
    }

    private int height(Node<E> n) {
        if (n == null) {
            return 0;
        }
        return n.getHeight();
    }

    private Node<E> rotateRight(Node<E> subRoot) {
        Node<E> temp = subRoot.getLeft();
        subRoot.setLeft(temp.getRight());
        temp.setRight(subRoot);
        subRoot.setHeight(Math.max(height(subRoot.getLeft()), height(subRoot.getRight())) + 1);
        temp.setHeight(Math.max(height(temp.getLeft()), height(temp.getRight())) + 1);
        return temp;
    }

    private Node<E> rotateLeft(Node<E> subRoot) {
        Node<E> temp = subRoot.getRight();
        subRoot.setRight(temp.getLeft());
        temp.setLeft(subRoot);
        subRoot.setHeight(Math.max(height(subRoot.getLeft()), height(subRoot.getRight())) + 1);
        temp.setHeight(Math.max(height(temp.getLeft()), height(temp.getRight())) + 1);
        return temp;
    }
}
