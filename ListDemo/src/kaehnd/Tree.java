/*
 * CS2852
 * Spring 2017
 * Name: Derek Riley
 * Created 4/6/2017 
 */

/**
 * Description: Tree class
 *
 * @author riley
 * @version 4/6/2017
 */

package kaehnd;

public class Tree<E extends Comparable<E>> {

    /**
     * This is the root of the tree
     */
    protected Node<E> root;

    public Tree(){
        super();
    }

    /**
     * Constructor sets the tree's root to data passed in
     */
    public Tree(E data) {
        root = new Node(data);
    }

    /**
     * Creates a tree with the root as its root
     * @param root
     */
    public Tree(Node root){
        this.root = root;
    }

    /**
     * returns the root of the tree
     * @return root
     */
    public Node<E> getRoot(){
        return root;
    }

    /**
     * Returns the left subtree
     * @return left subtree
     */
    public Tree<E> getLeftSubtree(){
        return new Tree(root.getLeft());
    }

    /**
     * Returns the right subtree
     * @return right Subtree
     */
    public Tree<E> getRightSubtree(){
        return new Tree(root.getRight());
    }

    /**
     * returns the data at the root
     * @return data at the root
     */
    public E getData(){
        return root.getData();
    }

    public void print() {
        TreePrinter.print(this.getRoot());
    }

    @Override
    public String toString(){
        return root.toString();
    }
}
