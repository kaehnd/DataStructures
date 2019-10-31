/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 6: WordSearch
 * Name: Daniel Kaehn
 * Created: 4/14/2019
 */
package msoe.kaehnd.lab6;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing the game board containing a 2-dimensional grid of characters
 */
public class GameBoard {

//    public static void main(String[] args) throws Exception{
//        AutoCompleter autoCompleter = new SortedArrayListAutoCompleter();
//        autoCompleter.initialize("words.txt");
//        autoCompleter.
//    }

    private AutoCompleter autoCompleter;
    private char [][] gameBoard;
    private int height;
    private int length;

    private static final int MAX_LENGTH = 15;

    /**
     * Constructs a GameBoard with an AutoCompleter
     * @param strategy class implementing the AutoCompleter interface
     */
    public GameBoard(AutoCompleter strategy) {

        try {
            strategy.getLastOperationTime();
            this.autoCompleter = strategy;
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("AutoCompleter must be initialized" +
                    "before used to construct a GameBoard");
        }
    }

    /**
     * Loads the text file representing a grid at the given Path
     * @param path path to the text file
     * @throws IOException thrown when Input or Output fails
     */
    public void load(Path path) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        try(Scanner in = new Scanner(path.toFile())) {
            while (in.hasNextLine()) {
                temp.add(in.nextLine());
            }
        }
        try {
            height = temp.size() - 1;
            length = temp.get(0).length() - 1;
            gameBoard = new char[height + 1][length + 1];

            for (int i = 0; i < gameBoard.length; i++) {
                for (int j = 0; j < gameBoard[0].length; j++) {
                    gameBoard[i][j] = temp.get(i).charAt(j);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Grid file should have regular" +
                    " rectangular dimensions");
        }
    }

    /**
     * Uses the recursiveSearch method to generate a List of all possible words
     * @return List<String> of words that can be made from the grid
     */
    public List<String> findWords() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                list.addAll(recursiveSearch(i, j, new boolean[height + 1][length + 1], ""));
            }
        }
        return list;
    }

    /*Recursively calls recursiveSearch until linear letter combinations starting
     at the first specified row and column are tried against the AutoCompleter*/
    private List<String> recursiveSearch(int row, int col,
                                         boolean[][] visitedFlags, String partialWord) {
        partialWord += gameBoard[row][col];
        visitedFlags[row][col] = true;

        ArrayList<String> tempList = new ArrayList<>();
        if (partialWord.length() > MAX_LENGTH ||
                autoCompleter.allThatBeginWith(partialWord).size() == 0) {
            return tempList;
        }
        if (partialWord.length() >= 3 && autoCompleter.contains(partialWord)) {
            tempList.add(partialWord);
        }

        if (row - 1 >= 0 && col - 1 >= 0 && !visitedFlags[row - 1][col - 1]) {
            tempList.addAll(recursiveSearch(row - 1, col - 1,
                    copyMatrix(visitedFlags), partialWord));
        }
        if (row - 1 >= 0 && !visitedFlags[row - 1][col]) {
            tempList.addAll(recursiveSearch(row - 1, col, copyMatrix(visitedFlags), partialWord));
        }
        if (row - 1 >= 0 && col + 1 <= length && !visitedFlags[row - 1][col + 1]) {
            tempList.addAll(recursiveSearch(row - 1, col + 1,
                    copyMatrix(visitedFlags), partialWord));
        }
        if (col - 1 >= 0 && !visitedFlags[row][col - 1]) {
            tempList.addAll(recursiveSearch(row, col - 1, copyMatrix(visitedFlags), partialWord));
        }
        if (col + 1 <= length && !visitedFlags[row][col + 1]) {
            tempList.addAll(recursiveSearch(row, col + 1, copyMatrix(visitedFlags), partialWord));
        }
        if (row + 1 <= height && col - 1 >= 0 && !visitedFlags[row + 1][col - 1]) {
            tempList.addAll(recursiveSearch(row + 1, col - 1,
                    copyMatrix(visitedFlags), partialWord));
        }
        if (row + 1 <= height && !visitedFlags[row + 1][col]) {
            tempList.addAll(recursiveSearch(row + 1, col, copyMatrix(visitedFlags), partialWord));
        }
        if (row + 1 <= height && col + 1 <= length && !visitedFlags[row + 1][col + 1]) {
            tempList.addAll(recursiveSearch(row + 1, col + 1,
                    copyMatrix(visitedFlags), partialWord));
        }
        return tempList;
    }

    private boolean[][] copyMatrix(boolean[][] matrix) {
        boolean[][] toReturn = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            toReturn[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return toReturn;
    }
}
