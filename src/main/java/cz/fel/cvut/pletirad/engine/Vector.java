package cz.fel.cvut.pletirad.engine;

import javafx.geometry.Point2D;

public class Vector extends Point2D {

    /**
     * Serializable Point2D
     * Every object needs to have empty constructor for Jackson
     */
    public Vector(double x, double y) {
        super(x, y);
    }

    public Vector() {
        super(-1000, -1000);
    }


    /**
     * @param point (Vector)
     * @return new Vector which is sum of point and vector it was called upon
     */
    public Vector add(Vector point) {
        Point2D result = super.add(point);
        return new Vector(result.getX(), result.getY());
    }

    /**
     * @param point (Vector)
     * @return new Vector which is difference between point and vector it was called upon
     */
    public Vector subtract(Vector point) {
        Point2D result = super.subtract(point);
        return new Vector(result.getX(), result.getY());
    }

    /**
     * @param multiplier (double)
     * @return new Vector which is product of multiplier and vector it was called upon
     */

    public Vector multiply(double multiplier) {
        Point2D result = super.multiply(multiplier);
        return new Vector(result.getX(), result.getY());
    }
}
