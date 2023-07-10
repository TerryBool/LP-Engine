package cz.fel.cvut.pletirad.game.enemies.wraith;

import cz.fel.cvut.pletirad.engine.graphics.SpriteSheet;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WraithAnimHandler {

    private boolean allLoaded;
    private final double scalingFactor = 2;
    private boolean playOnce;
    private long stopTime;
    private int facing;
    private int numOfFrames;


    private SpriteSheet moveL;
    private SpriteSheet moveR;
    private boolean inCombat = false;

    private SpriteSheet death;
    private SpriteSheet idle;
    private SpriteSheet attack;

    private SpriteSheet currentAnim;

    public WraithAnimHandler() {
        try {
            moveL = new SpriteSheet(new Image("Wraith/Wraith move left.png"), 4, 1,
                    4, 48, 48, 10, scalingFactor);

            moveR = new SpriteSheet(new Image("Wraith/Wraith move right.png"), 4, 1,
                    1, 48, 48, 10, scalingFactor);

            death = new SpriteSheet(new Image("Wraith/Wraith death.png"), 8, 1,
                    8, 48, 48, 15, scalingFactor);

            attack = new SpriteSheet(new Image("Wraith/Wraith attack.png"), 10, 1,
                    10, 48, 48, 15, scalingFactor);

            idle = new SpriteSheet(new Image("Wraith/Wraith idle.png"), 4, 1,
                    4, 48, 48, 15, scalingFactor);

            allLoaded = true;
        } catch (Exception e) {
            allLoaded = false;
            System.err.println("Animation load for Player failed!!");
            System.err.println(e.getMessage());
            System.err.println();
        }
        if (allLoaded) {
            currentAnim = moveL;
            currentAnim.start();
        }
    }

    public boolean isAllLoaded() {
        return allLoaded;
    }

    public void drawAnim(GraphicsContext gc, Point2D pos) {
        if (!allLoaded) {
            return;
        }

        if (playOnce) {
            if (System.currentTimeMillis() > stopTime) {
                currentAnim.stop();
                if (inCombat) {
                    pIdle();
                } else {
                    pMove(facing);
                }
                currentAnim.start();
                playOnce = false;
            }
        }
        currentAnim.drawSprite(gc, pos);
    }

    public void pMove(int dir) {
        if (dir < 0) {
            if (moveL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (moveR.equals(currentAnim)) {
                return;
            }
        }
        currentAnim.stop();
        if (dir < 0) {
            currentAnim = moveL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = moveR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.start();
    }

    public void pIdle() {

        if (idle.equals(currentAnim)) {
            return;
        }

        playOnce = false;
        currentAnim.stop();
        currentAnim = idle;
        currentAnim.start();
    }

    public void pAttack() {
        if (attack.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = attack;

        playOnce = true;
        inCombat = true;
        numOfFrames = 10;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 15;

        currentAnim.start();
    }

    public void pDeath() {
        if (death.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = death;

        playOnce = true;
        inCombat = false;
        numOfFrames = 8;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 15;

        currentAnim.start();
    }

    public void setScalingFactor(double scalingFactor) {
        moveL.setScalingFactor(scalingFactor);
        moveR.setScalingFactor(scalingFactor);
        death.setScalingFactor(scalingFactor);
        idle.setScalingFactor(scalingFactor);
        attack.setScalingFactor(scalingFactor);
    }
}

