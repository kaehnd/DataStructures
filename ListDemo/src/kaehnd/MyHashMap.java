package kaehnd;

/**
 * Description: Hash Table class with overwriting for collisions
 *
 * @author riley
 * @version 4/28/2019
 */

public class MyHashMap<K, V> {

    /**
     * table to hold the hashed values
     */
    private V table [];

    /**
     * Parameter to keep track of the allocated size of the table
     */
    private int tableSize;

    /**
     * Parameter to keep track of the number of elements currently in the table
     */
    private int size;

    /**
     * Initializes a new Hash Map with a table size of 0
     */
    public MyHashMap(){
        tableSize = 20;
        size = 0;
        table = (V[]) new Object[tableSize];
    }

    /**
     * empties the hash map
     */
    public void clear(){
        size = 0;
        table = (V[]) new Object[tableSize];
    }

    /**
     * Adds an element to the Array of hashed items
     * Note: collisions are handled by overwriting
     * @param key key location to be used to calculate the hash value
     * @param value value to be store
     */
    public void add(K key, V value) {
        //TODO handle collisions gracefully
        int index = key.hashCode()%tableSize;
        if(table[index] != null){
            System.out.println("collision!");
            size--;  //this collision doesn't grow the size of the table
        }
        table[index] = value;
        size++;
    }

    /**
     * Determines if an element exists at the key location
     * @param key code representing the location
     * @return the item at the location
     */
    public V contains(K key){
        int index = key.hashCode()%tableSize;
        return table[index];
    }

    /**
     * Returns whether the data structure is empty
     * @return true if the data structure is empty
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Returns the number of elements in the data structure
     * @return
     */
    public int size(){
        return size;
    }

    /**
     * This method removes an element and returns it
     * @param key the key for the element to be removed
     * @return the element removed
     */
    public V remove(Object key){
        //TODO handle collisions gracefully
        size--;
        int index = key.hashCode()%tableSize;
        V item = table[index];
        table[index] = null;
        return item;
    }

    /**
     * This method returns the string representation of the data structure (unstructured)
     * @return a String representing the data structure
     */
    @Override
    public String toString(){
        String string = "";
        for(V item:table){
            if(item != null) {
                string += item.toString()+"\n";
            }
            else{
                string += "null \n";
            }
        }
        return string;
    }
}