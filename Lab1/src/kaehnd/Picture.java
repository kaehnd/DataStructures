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
import java.util.*;

/**
 * Stores a List of Dots and implements drawing and opening methods to a Canvas
 */
public class Picture {


    private List<Dot> dotList;
    private GraphicsContext graphicsContext;

    private final Color black;


    /**
     * Consteructs a Picture, instantiates Color
     */
    public Picture(){
        black = new Color(0, 0, 0, 1);
    }


    /**
     * Loads dot information from file and creates List of Dots
     * @param file User-selected File object where dot information is stored
     * @throws IOException Thrown when file loading error occurs
     * @throws InputMismatchException Thrown when .dot data is malformed or corrupted
     * @throws IllegalArgumentException Thrown when file extension is not .dot
     */
    public void load(File file) throws IOException,
            InputMismatchException, IllegalArgumentException{

        //Checks if file extension is ".dot"
        if (!file.getAbsolutePath().substring(
                file.getAbsolutePath().lastIndexOf('.')).equals(".dot")) {
            throw new IllegalArgumentException("Non '.dot' file chosen");
        }

        dotList = new ArrayList();

        try(Scanner in = new Scanner(file)){
            while(in.hasNextLine()){
                dotList.add(stringToDot(in.nextLine()));
            }
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
            double x = dot.getX() * canvas.getWidth() - (double) dotDiameter/2;
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
        Scanner reader = new Scanner(s);
        double x = reader.nextDouble();
        double y = reader.nextDouble();
        return new Dot(x, y);
    }
}
