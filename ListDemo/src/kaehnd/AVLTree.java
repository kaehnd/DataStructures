package kaehnd;

public class AVLTree<E extends Comparable<E>> extends BinarySearchTree {


    public Node rotateRight(Node subRoot) {
        Node temp = subRoot.getLeft();
        subRoot.setLeft(temp.getRight());
        temp.setRight(subRoot);
        return temp;
    }

    private Node rotateLeft(Node subRoot) {
        Node temp = subRoot.getRight();
        subRoot.setRight(temp.getLeft());
        temp.setLeft(subRoot);
        return temp;
    }
}
