/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 1: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/6/2019
 */
package kaehnd;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;


/**
 * Stores a List of Dots and implements drawing and opening methods to a Canvas
 */
public class Picture {


    private List<Dot> dotList;
    private GraphicsContext graphicsContext;

    private final Color black;

    /**
     * Constructs a Picture object using a List<Dot> of any List type
     * @param dots List<Dot> of any List subclass
     */
    public Picture(List<Dot> dots) {
        black = new Color(0, 0, 0, 1);
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
     * @param numberDesired desired number of dots at the end of the algorithm
     */
    public void removeDots(int numberDesired) {
        if (numberDesired < 3) {
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
    }

    /**
     * Draws only dots on the Canvas
     * @param canvas Canvas object to be drawn upon
     */
    public void drawDots(Canvas canvas) {
        final int dotDiameter = 5;
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(black);
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
        graphicsContext.setFill(black);
        graphicsContext.beginPath();

        for (int i = 0; i < dotList.size() - 1; i++) {
            double x = dotList.get(i).getX() * canvas.getWidth();
            double y = canvas.getHeight() - dotList.get(i).getY() * canvas.getHeight();
            graphicsContext.moveTo(x, y);

            x = dotList.get(i + 1).getX() * canvas.getWidth();
            y = canvas.getHeight() - dotList.get(i + 1).getY() * canvas.getHeight();
            graphicsContext.lineTo(x, y);
        }
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    private Dot stringToDot(String s) throws NoSuchElementException {
        s = s.replace(',', ' ');
        try (Scanner reader = new Scanner(s)) {
            double x = reader.nextDouble();
            double y = reader.nextDouble();
            return new Dot(x, y);
        }
    }
}
