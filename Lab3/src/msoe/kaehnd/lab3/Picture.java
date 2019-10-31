/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 3: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/16/2019
 */
package msoe.kaehnd.lab3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * Stores a List of Dots and implements drawing and opening methods to a Canvas
 */
public class Picture {


    private List<Dot> dotList;
    private GraphicsContext graphicsContext;
    private static final int MIN_NUM_DOTS = 3;

    /**
     * Constructs a Picture object using a List<Dot> of any List type
     * @param dots List<Dot> of any List subclass
     */
    public Picture(List<Dot> dots) {
        dotList = dots;
    }

    /**
     * Constructs a Picture using another Picture and a List<Dot> of any List type
     * @param original original Picture object whose dots are to be copied to dots
     * @param dots List<Dot> of any List subclass
     */
    public Picture(Picture original, List<Dot> dots) {
        this(dots);
        dotList.addAll(original.dotList);
    }

    public int getNumDots(){
        return dotList.size();
    }

    /**
     * Loads dot information from file and creates List of Dots
     * @param file User-selected File object where dot information is stored
     * @throws IOException Thrown when file loading error occurs
     * @throws InputMismatchException Thrown when .dot data is malformed or corrupted
     * @throws IllegalArgumentException Thrown when file extension is not .dot
     */
    public void load(File file) throws IOException,
            InputMismatchException, IllegalArgumentException {
        //Checks if file extension is ".dot"
        if (!file.getAbsolutePath().substring(
                file.getAbsolutePath().lastIndexOf('.')).equals(".dot")) {
            throw new IllegalArgumentException("Non '.dot' file chosen");
        }
        try(Scanner in = new Scanner(file)) {
            while(in.hasNextLine()){
                dotList.add(stringToDot(in.nextLine()));
            }
        }
    }

    /**
     * Saves the Picture to a .dot file
     * @param file file to be saved to
     * @throws IOException thrown when file output fails
     */
    public void save(File file) throws IOException{
        try(PrintWriter printWriter = new PrintWriter(file)) {
            for (Dot dot: dotList) {
                printWriter.println(dot);
            }
        }
    }

    /**
     * Removes or adds dots down to three and up to dots of original picture
     * Using index-based methods
     * @param numberDesired desired number of dots at the end of the operation
     * @return timeInMillis time taken to run method
     */
    public long removeDots(int numberDesired) {
        long initialTime = System.nanoTime();
        if (numberDesired < MIN_NUM_DOTS) {
            throw new IllegalArgumentException("Dot value must be greater than or equal to 3");
        }
        while (dotList.size() > numberDesired) {

            int smallestDotIndex = 0;

            //Deal with first Dot wrapping to last Dot
            double smallestCritValue = dotList.get(0).calculateCriticalValue(
                    dotList.get(dotList.size() - 1), dotList.get(1));

            //Iterate through every Dot and find Dot with smallest Critical Value
            for (int i = 1; i < dotList.size() - 1; i++) {
                double currentCrit = dotList.get(i).calculateCriticalValue(
                        dotList.get(i - 1), dotList.get(i + 1));

                if (currentCrit < smallestCritValue) {
                    smallestCritValue = currentCrit;
                    smallestDotIndex = i;
                }
            } // Deal with last Dot wrapping to first Dot
            double currentCrit = dotList.get(dotList.size() - 1).calculateCriticalValue(
                    dotList.get(dotList.size() - 2), dotList.get(0));

            if (currentCrit < smallestCritValue) {
                smallestDotIndex = dotList.size() - 1;
            }
            dotList.remove(smallestDotIndex);
        }
        return System.nanoTime() - initialTime;
    }


    /**
     * Removes or adds dots down to three and up to dots of original picture
     * using iterator-based methods
     * @param numberDesired desired number of dots at the end of the operation
     * @return timeInMillis time taken to run method
     */
    public long removeDots2(int numberDesired) {
        long initialTime = System.nanoTime();
        if (numberDesired < MIN_NUM_DOTS) {
            throw new IllegalArgumentException("Dot value must be greater than or equal to 3");
        }
        Collection<Dot> dotList = this.dotList;


        while (dotList.size() > numberDesired) {
            Dot smallestDot = null;
            double smallestCritValue = Integer.MAX_VALUE;

            Iterator<Dot> dotIterator = dotList.iterator();

            Dot firstDot = dotIterator.next();
            Dot secondDot = dotIterator.next();

            Dot prevDot = firstDot;
            Dot currentDot = secondDot;
            Dot nextDot = dotIterator.next();

            while (dotIterator.hasNext()) {
                double currentCrit = currentDot.calculateCriticalValue(
                        prevDot, nextDot);

                if (currentCrit < smallestCritValue) {
                    smallestCritValue = currentCrit;
                    smallestDot = currentDot;
                }

                prevDot = currentDot;
                currentDot = nextDot;
                nextDot = dotIterator.next();

            }
            //Handle last Dot wrapping to first Dot
            double currentCrit = nextDot.calculateCriticalValue(
                    currentDot, firstDot);

            if (currentCrit < smallestCritValue) {
                smallestCritValue = currentCrit;
                smallestDot = nextDot;
            }
            //handle first Dot wrapping to last Dot
            currentCrit = firstDot.calculateCriticalValue(
                    nextDot, secondDot);

            if (currentCrit < smallestCritValue) {
                smallestCritValue = currentCrit;
                smallestDot = firstDot;
            }
            dotList.remove(smallestDot);
        }
        return System.nanoTime() - initialTime;
    }


    /**
     * Draws only dots on the Canvas
     * @param canvas Canvas object to be drawn upon
     */
    public void drawDots(Canvas canvas) {
        final int dotDiameter = 5;
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        for (Dot dot: dotList) {
            double x = dot.getX() * canvas.getWidth() - (double) dotDiameter / 2;
            double y = canvas.getHeight() - (dot.getY() * canvas.getHeight())
                    - ((double) dotDiameter / 2);
            graphicsContext.fillOval(x, y, dotDiameter, dotDiameter);
        }
    }

    /**
     * Draws only lines that would connect dots to the Canvas
     * @param canvas Canvas object to be drawn upon
     */
    public void drawLines(Canvas canvas) {
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.beginPath();

        double xInitial = dotList.get(0).getX() * canvas.getWidth();
        double yInitial = canvas.getHeight() - dotList.get(0).getY() * canvas.getHeight();
        graphicsContext.moveTo(xInitial, yInitial);

        for (int i = 1; i < dotList.size(); i++) {
            double x = dotList.get(i).getX() * canvas.getWidth();
            double y = canvas.getHeight() - dotList.get(i).getY() * canvas.getHeight();
            graphicsContext.lineTo(x, y);
        }
        //Draw line connecting last and first dots
        graphicsContext.lineTo(xInitial, yInitial);
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    private Dot stringToDot(String s) throws NoSuchElementException {
        s = s.replace(',', ' ');
        try (Scanner reader = new Scanner(s)) {
            double x = reader.nextDouble();
            double y = reader.nextDouble();
            if (y < 0 || x < 0) {
                throw new IllegalArgumentException("Dot coordinates in file cannot be negative");
            }
            return new Dot(x, y);
        }
    }
}
