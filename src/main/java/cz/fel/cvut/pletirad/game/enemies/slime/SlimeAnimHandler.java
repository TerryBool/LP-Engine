package cz.fel.cvut.pletirad.game.enemies.slime;

import cz.fel.cvut.pletirad.engine.graphics.SpriteSheet;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Handler for all slime animations
 * Extremely similar to players so just go peek at that one
 */

public class SlimeAnimHandler {

    private boolean allLoaded;
    private final double scalingFactor = 1.5;
    private boolean playOnce;
    private int facing;
    private int numOfFrames;


    private SpriteSheet moveL;
    private SpriteSheet moveR;
    private boolean inCombat = false;

    private SpriteSheet death;
    private SpriteSheet idle;
    private SpriteSheet attack;

    private SpriteSheet currentAnim;

    public SlimeAnimHandler() {
        try {
            moveL = new SpriteSheet(new Image("Slime/blob move.png"), 8, 1,
                    8, 80, 80, 6, scalingFactor);

            moveR = new SpriteSheet(new Image("Slime/blob move right.png"), 8, 1,
                    8, 80, 80, 6, scalingFactor);

            death = new SpriteSheet(new Image("Slime/blob death.png"), 7, 1,
                    7, 80, 80, 10, scalingFactor);

            attack = new SpriteSheet(new Image("Slime/blob attack.png"), 10, 1,
                    10, 80, 80, 6, scalingFactor);

            idle = new SpriteSheet(new Image("Slime/blob idle.png"), 8, 1,
                    8, 80, 80, 6, scalingFactor);

            allLoaded = true;
        } catch (Exception e) {
            allLoaded = false;
            System.err.println("Animation load for Player failed!!");
            System.err.println(e.getMessage());
            System.err.println();
        }
        if (allLoaded) {
            currentAnim = moveL;
            currentAnim.resetAnim();
        }
    }

    public boolean isAllLoaded() {
        return allLoaded;
    }

    public void drawAnim(GraphicsContext gc, Point2D pos) {
        if (!allLoaded || currentAnim == null) {
            return;
        }

        if (playOnce) {
            if (numOfFrames == 0) {
                currentAnim.resetAnim();
                if (inCombat) {
                    if (death.equals(currentAnim)) {
                        currentAnim = null;
                        playOnce = false;
                    } else {
                        pIdle();
                    }
                } else {
                    pMove(facing);
                }
            } else {
                numOfFrames--;
            }
        }

        if (currentAnim != null) {
            currentAnim.nextFrame();
            currentAnim.drawSprite(gc, pos);
        }
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
        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = moveL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = moveR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    public void pIdle() {

        if (idle.equals(currentAnim)) {
            return;
        }
        currentAnim.resetAnim();

        currentAnim = idle;
        playOnce = false;

        currentAnim.resetAnim();
    }

    public void pAttack() {
        if (attack.equals(currentAnim)) {
            return;
        }

        currentAnim.resetAnim();
        currentAnim = attack;

        playOnce = true;
        inCombat = true;

        numOfFrames = currentAnim.getTotalFrames()*currentAnim.getWaitFrames();

        currentAnim.resetAnim();
    }

    public void pDeath() {
        if (death.equals(currentAnim)) {
            return;
        }

        currentAnim.resetAnim();
        currentAnim = death;

        playOnce = true;
        inCombat = true;

        numOfFrames = currentAnim.getTotalFrames()*currentAnim.getWaitFrames();

        currentAnim.resetAnim();
    }

    public void setScalingFactor(double scalingFactor) {
        moveL.setScalingFactor(scalingFactor);
        moveR.setScalingFactor(scalingFactor);
        death.setScalingFactor(scalingFactor);
        idle.setScalingFactor(scalingFactor);
        attack.setScalingFactor(scalingFactor);
    }
}

