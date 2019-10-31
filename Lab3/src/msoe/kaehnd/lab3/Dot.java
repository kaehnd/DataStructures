/*
 * Course: CS2852
 * Term: Spring 2019
 * Lab 3: Dot2Dot
 * Name: Daniel Kaehn
 * Created: 3/16/2019
 */
package msoe.kaehnd.lab3;

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

    /**
     * Calculates and returns the critical value of a Dot object
     * based upon its positon relative to the previous and next Dot
     * @param prevous previous Dot
     * @param next next Dot
     * @return double critical value of the Dot
     */
    public double calculateCriticalValue(Dot prevous, Dot next){
        double prevNext = distanceBetween(prevous, next);
        double thisPrev = distanceBetween(this, prevous);
        double thisNext = distanceBetween(this, next);
        return thisNext + thisPrev - prevNext;
    }

    private double distanceBetween(Dot dot1, Dot dot2){
        double x = Math.pow(dot1.getX() - dot2.getX(), 2);
        double y = Math.pow(dot1.getY() - dot2.getY(), 2);
        return Math.sqrt(x + y);
    }

    /**
     * Converts dot object to file writable String
     * @return String in the format "<x-coord>,<y-coord>"
     */
    @Override
    public String toString() {
        return getX() + ", " + getY();
    }

}
