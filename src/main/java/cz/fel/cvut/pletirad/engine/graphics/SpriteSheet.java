package cz.fel.cvut.pletirad.engine.graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * SpriteSheet animation heroically liberated from beautiful stranger on the internet
 * Some changes have been made so it works with our project
 * All credits to Tryder
 * <ahref>https://stackoverflow.com/questions/10708642/javafx-2-0-an-approach-to-game-sprite-animation</ahref>
 */

public class SpriteSheet {

    private Image spriteSheet;

    private final int totalFrames; //Total number of frames in the sequence
    private final int waitFrames; // How many frames to wait till next update
    private double scalingFactor; // Enlargement of sprite

    private final int cols; //Number of columns on the sprite sheet
    private final int rows; //Number of rows on the sprite sheet

    private final int frameWidth; //Width of an individual frame
    private final int frameHeight; //Height of an individual frame

    private int currentCol = 0;
    private int currentRow = 0;
    private int timesCalled = 0;

    private long lastFrame = 0;

    public SpriteSheet(Image image, int columns, int rows, int totalFrames, int frameWidth, int frameHeight, int waitFrames, double scalingFactor) {
        spriteSheet = image;

        cols = columns;
        this.waitFrames = waitFrames;
        this.scalingFactor = scalingFactor;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public void nextFrame() {
        if (timesCalled < waitFrames) {
            timesCalled += 1;
            return;
        }
        // Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
        int frameJump = 1;
        int addRows = (int) Math.floor((float) frameJump / (float) cols);
        int frameAdd = frameJump - (addRows * cols);

        if (currentCol + frameAdd >= cols) {
            currentRow += addRows + 1;
            currentCol = frameAdd - (cols - currentCol);
        } else {
            currentRow += addRows;
            currentCol += frameAdd;
        }
        currentRow = (currentRow >= rows) ? currentRow - ((int) Math.floor((float) currentRow / rows) * rows) : currentRow;

        //The last row may or may not contain the full number of columns
        if ((currentRow * cols) + currentCol >= totalFrames) {
            currentRow = 0;
            currentCol = Math.abs(currentCol - (totalFrames - (int) (Math.floor((float) totalFrames / cols) * cols)));
        }

        timesCalled = 0;
    }

    public void resetAnim() {
        currentCol = 0;
        currentRow = 0;
    }

    /**
     * Draws certain sprite from sprite sheet with added scaling factor for enlargement
     *
     * @param gc  Graphics context of canvas
     * @param pos Position to draw the sprite
     */
    public void drawSprite(GraphicsContext gc, Point2D pos) {
        gc.drawImage(spriteSheet, currentCol * frameWidth, currentRow * frameHeight,
                frameWidth, frameHeight, pos.getX(), pos.getY(),
                frameWidth * scalingFactor, frameHeight * scalingFactor);
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public int getWaitFrames() {
        return waitFrames;
    }
}
