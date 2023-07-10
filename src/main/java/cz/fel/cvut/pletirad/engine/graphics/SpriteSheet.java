package cz.fel.cvut.pletirad.engine.graphics;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteSheet extends AnimationTimer {

    Image spriteSheet;

    private final int totalFrames; //Total number of frames in the sequence
    private final float fps; //frames per second I.E. 24
    private double scalingFactor;

    private final int cols; //Number of columns on the sprite sheet
    private final int rows; //Number of rows on the sprite sheet

    private final int frameWidth; //Width of an individual frame
    private final int frameHeight; //Height of an individual frame

    private int currentCol = 0;
    private int currentRow = 0;

    private long lastFrame = 0;

    public SpriteSheet(Image image, int columns, int rows, int totalFrames, int frameWidth, int frameHeight, float framesPerSecond, double scalingFactor) {
        spriteSheet = image;

        cols = columns;
        this.scalingFactor = scalingFactor;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        fps = framesPerSecond;

        lastFrame = System.nanoTime();
    }

    @Override
    public void handle(long now) {
        int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps)); //Determine how many frames we need to advance to maintain frame rate independence

        // Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
        if (frameJump >= 1) {
            lastFrame = now;
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
        }
    }

    @Override
    public void stop() {
        super.stop();
        currentRow = 0;
        currentCol = 0;
    }

    public void drawSprite(GraphicsContext gc, Point2D pos) {
        gc.drawImage(spriteSheet, currentCol * frameWidth, currentRow * frameHeight,
                frameWidth, frameHeight, pos.getX(), pos.getY(),
                frameWidth * scalingFactor, frameHeight * scalingFactor);
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }
}
