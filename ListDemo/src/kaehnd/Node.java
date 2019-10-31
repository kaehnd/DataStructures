/*
 * CS2852
 * Spring 2019
 * Name: Derek Riley
 * Created 4/6/2019
 */

/**
 * Description: Node of a binary tree
 *
 * @author riley
 * @version 4/6/2019
 */

package kaehnd;

public class Node<E extends Comparable<E>> implements TreePrinter.PrintableNode{

    /**
     * Instance variables here
     */
    private Node<E> right;
    private Node<E> left;
    private E data;

    /**
     * Default constructor puts data into the node
     */
    public Node(E data) {
        this.data = data;
        left = null;
        right = null;
    }

    /**
     * Get the right node
     * @return right node
     */
    public Node<E> getRight() {
        return right;
    }

    /**
     * Set the right node
     * @param right node
     */
    public void setRight(Node<E> right) {
        this.right = right;
    }

    /**
     * get the left node
     * @return left node
     */
    public Node<E> getLeft() {
        return left;
    }

    /**
     * set the left node
     * @param left node
     */
    public void setLeft(Node<E> left) {
        this.left = left;
    }

    /**
     * get the data stored in the node
     * @return data
     */
    public E getData() {
        return data;
    }

    /**
     * set the data stored in the node
     * @param data to be stored
     */
    public void setData(E data) {
        this.data = data;
    }


    public int numDescendents() {
        return numDescendents(this);
    }


    private int numDescendents(Node current) {
        int leftDescendents = 0;
        int rightDescendents = 0;

        if(current.getRight() == null && current.getLeft() == null){
            return 0;
        }
        if(current.getLeft() != null){
            leftDescendents = numDescendents(current.getLeft()) + 1;
        }
        if(current.getRight() != null){
            rightDescendents = numDescendents(current.getRight()) + 1;
        }
        return leftDescendents + rightDescendents;
    }





    /**
     * Calculates the height/depth of a tree
     * @return an int representing the height/depth
     */
    public int height(){
        return height(this);
    }

    private int height(Node current) {
        int leftHeight = 0;
        int rightHeight = 0;
        if(current.getRight() == null && current.getLeft() == null){
            return 1;
        }
        if(current.getLeft() != null){
            leftHeight = height(current.getLeft());
        }
        if(current.getRight() != null){
            rightHeight = height(current.getRight());
        }
        return Math.max(leftHeight,rightHeight) + 1;
    }


    @Override
    public String toString(){
        String output = "";

        if(left != null){
            output += left;
        }
        if(data != null){
            output += data.toString();
            output += " ";
        }
        if(right != null){
            output += right;
        }
        return output;
    }

    public String getText() {
        return this.getData().toString();
    }
}
