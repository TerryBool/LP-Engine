package cz.fel.cvut.pletirad.engine.inputs;


import javafx.geometry.Rectangle2D;

public class MenuButton extends Rectangle2D {

    private final String title;

    /**
     * Creates a new instance of {@code Rectangle2D}.
     *
     * @param minX   The x coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param minY   The y coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param width  The width of the {@code Rectangle2D}
     * @param height The height of the {@code Rectangle2D}
     */
    public MenuButton(double minX, double minY, double width, double height, String title) {
        super(minX, minY, width, height);
        this.title = title;
    }

    public String getName() {
        return title;
    }
}
