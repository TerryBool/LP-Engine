package cz.fel.cvut.pletirad.engine;

import javafx.geometry.Point2D;

public class Vector extends Point2D {
    /**
     * Creates a new instance of {@code Point2D}.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public Vector(double x, double y) {
        super(x, y);
    }

    public Vector() {
        super(-1000, -1000);
    }

    public Vector add(Vector point) {
        Point2D result = super.add(point);
        return new Vector(result.getX(), result.getY());
    }

    public Vector subtract(Vector point) {
        Point2D result = super.subtract(point);
        return new Vector(result.getX(), result.getY());
    }

    public Vector multiply(double multiplier) {
        Point2D result = super.multiply(multiplier);
        return new Vector(result.getX(), result.getY());
    }
}
