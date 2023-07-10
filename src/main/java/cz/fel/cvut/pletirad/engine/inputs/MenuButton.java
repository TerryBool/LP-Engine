package cz.fel.cvut.pletirad.engine.inputs;


import javafx.geometry.Rectangle2D;

/**
 * Rectangle2D but with name.
 * Helps with drawing buttons in menus
 */

public class MenuButton extends Rectangle2D {

    private final String title;

    public MenuButton(double minX, double minY, double width, double height, String title) {
        super(minX, minY, width, height);
        this.title = title;
    }

    public String getName() {
        return title;
    }
}
