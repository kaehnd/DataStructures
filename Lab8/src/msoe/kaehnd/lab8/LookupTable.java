/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 8: MorseEncoder
 * Name: Daniel Kaehn
 * Created: 5/1/2019
 */

package msoe.kaehnd.lab8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Table making use of a sorted ArrayList and BinarySearch rather than hashing
 * to create a table of quickly accessible key/value pairs
 * @param <K> Key type Generic
 * @param <V> Value type Generic
 */
public class LookupTable<K extends Comparable<?super K>, V> implements Map<K, V> {

    private List<Entry<K, V>> table;

    private class Entry<K extends Comparable<?super K>, V>
            implements Comparable<Entry<K, V>>, Map.Entry<K, V> {

        private K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V toReturn = this.value;
            this.value = value;
            return toReturn;
        }

        /**
         * Compares two Entries based upon their keys
         * @param other another Entry object
         * @return integer indicating comparison status
         */
        @Override
        public int compareTo(Entry<K, V> other) {
            return key.compareTo(other.key);
        }
    }

    /**
     * Constructs a LookupTable
     */
    public LookupTable(){
        table = new ArrayList<>();
    }

    /**
     * Returns the size of the table
     * @return integer size of the table
     */
    @Override
    public int size() {
        return table.size();
    }


    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    /**
     * Determines whether the table contains an Entry with the specified key
     * @param key key of Generic type K
     * @return boolean if Entry with that key is contained
     */
    @Override
    public boolean containsKey(Object key) {
        try {
            return Collections.binarySearch(table, new Entry<>((K) key, null)) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Unsupported operation
     * @param value N/A
     * @return N/A
     */
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Operation not supported");
    }


    /**
     * Returns the Value associated with the specified key
     * @param key key of Generic type K
     * @return the value of Generic type V if found, or null if not found or key is the wrong type
     */
    @Override
    public V get(Object key) {
        try {
            int index = Collections.binarySearch(table, new Entry<>((K) key, null));
            if (index < 0) {
                return null;
            }
            return table.get(index).value;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Inserts a new element pair into the table
     * @param key key mapped to value of generic type K
     * @param value value stored of generic type V
     * @return value previously stored at that key or null
     */
    @Override
    public V put(K key, V value) {
        Entry<K, V> toAdd = new Entry<>(key, value);
        V toReturn;
        int index = Collections.binarySearch(table, toAdd);

        //Deals with cases where the desired index is at the end or beginning
        if (index < 0) {
            index++;
            index *= -1;
            toReturn = null;
        } else {
            toReturn = table.get(index).value;
        }
        table.add(index, toAdd);
        return toReturn;
    }

    /**
     * Removes the Entry associated with the specified key
     * @param key key object of generic type K
     * @return the value of type V associated with the removed Entry or null if not found
     */
    @Override
    public V remove(Object key) {
        try {
            int index = Collections.binarySearch(table, new Entry<>((K) key, null));
            if (index < 0) {
                return null;
            }
            V toReturn = table.get(index).value;
            table.remove(index);
            return toReturn;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     *  Unsupported Operation
     * @param m N/A
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    /**
     * Clears the table
     */
    @Override
    public void clear() {
        table.clear();
    }

    /**
     * Unsupported operation
     * @return N/A
     */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    /**
     * Unsupported operation
     * @return N/A
     */
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    /**
     * Unsupported Operation
     * @return N/A
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Operation not supported");
    }
}