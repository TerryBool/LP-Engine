package cz.fel.cvut.pletirad.engine;

import javafx.geometry.Rectangle2D;

public class HitBox extends Rectangle2D {


    /**
     * Serializable Rectangle2D
     * Every object needs to have empty constructor for Jackson
     */
    public HitBox(double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }

    public HitBox() {
        super(-1000, -1000, 0, 0);
    }
}
