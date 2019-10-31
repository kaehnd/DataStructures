/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 1: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/6/2019
 */
package kaehnd;

/**
 * Class representing a singular dot in a Picture object
 */
public class Dot {

    private double x;
    private double y;

    /**
     * Constructs a Dot with specified coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Dot(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
