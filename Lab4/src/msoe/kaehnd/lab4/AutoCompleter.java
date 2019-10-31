/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 4: AutoComplete
 * Name: Daniel Kaehn
 * Created: 3/28/2019
 */
package msoe.kaehnd.lab4;

import java.io.IOException;
import java.util.List;

/**
 * Interface describing functionality of all classes
 * used to auto-complete words for AutoComplete Application
 */
public interface AutoCompleter {

    /**
     * Initializes AutoCompleter with a dictionary at the given filename
     * @param filename name of the file
     * @throws IOException when file cannot be loaded
     */
    void initialize(String filename) throws IOException;

    /**
     * Returns a List<String> of every String in dictionary List<String> beginning with the prefix
     * @param prefix String to check with every List item
     * @return a List<String> including every String beginning with prefix
     */
    List<String> allThatBeginWith(String prefix);

    /**
     * Returns the time taken to perform the last operation
     * @return long time in nanoseconds
     */
    long getLastOperationTime();

}
