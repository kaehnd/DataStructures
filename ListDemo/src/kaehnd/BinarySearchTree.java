/*
 * CS2852
 * Spring 2019
 * Name: Derek Riley
 * Created 4/5/2019
 */

package kaehnd;
/**
 * Description: Binary Search Tree class
 *
 * @author riley
 * @version 4/5/2019
 */
public class BinarySearchTree<E extends Comparable<E>> extends Tree<E> {

    /**
     * Default constructor
     */
    public BinarySearchTree(){
        super();
    }

    /**
     * add method (recursive)
     * @param item item to be added
     * @return true if the insert is valid
     */
    public boolean add(E item){
        if(root == null){
            root = new Node(item);


            return true;
        }
        else {
            return add(root, item);
        }
    }

    private boolean add(Node current, E item){
        //TODO
        int comparison = current.getData().compareTo(item);


        if(comparison == 0){
            return true;
        }
        else if (comparison > 0){
            //new item goes on the left branch...

            if (current.getLeft() == null) {
                current.setLeft(new Node(item));
                return true;
            } else {
                return add(current.getLeft(), item);
            }
        }
        else {
            if (current.getRight() == null) {
                current.setRight(new Node(item));
                return true;
            } else {
                return add(current.getRight(), item);
            }
        }
    }

    /**
     * Searches to find if an element is contained in the BST
     * @param target item to be found
     * @return true if the item is found, false if not
     */
    public boolean contains(E target){
        return contains(root, target);
    }

    private boolean contains(Node current, E target){
        int comparison = current.getData().compareTo(target);
        if(comparison == 0){
            return true;
        }
        else if (comparison > 0){
            if (current.getLeft() == null) {
                return false;
            }
            return contains(current.getLeft(), target);
        }
        else{
            if (current.getRight() == null) {
                return false;
            }
            return contains(current.getRight(), target);
        }
    }

    /**
     * Calculates the total number of nodes
     * @return an int representing the size
     */
    public int size(){
        return size(root);
    }

    private int size(Node current){
        //TODO
        return -1;
    }

    /**
     * Calculates the height/depth of a tree
     * @return an int representing the height/depth
     */
    public int height(){
        return height(root);
    }

    private int height(Node current) {
        //TODO
        return -1;
    }
    /**
     * creates a string representation of a tree
     * @return a string of the treee
     */
    @Override
    public String toString(){
        return root.toString();
    }
}
