package cz.fel.cvut.pletirad.engine;

import javafx.geometry.Rectangle2D;

public class HitBox extends Rectangle2D {


    /**
     * Creates a new instance of {@code Rectangle2D}.
     *
     * @param minX   The x coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param minY   The y coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param width  The width of the {@code Rectangle2D}
     * @param height The height of the {@code Rectangle2D}
     */
    public HitBox(double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }

    public HitBox() {
        super(-1000, -1000, 0, 0);
    }
}
