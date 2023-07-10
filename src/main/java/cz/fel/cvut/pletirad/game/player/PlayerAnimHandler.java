package cz.fel.cvut.pletirad.game.player;

import cz.fel.cvut.pletirad.engine.graphics.SpriteSheet;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Holds every single animation for player and handles the way they are played
 */

public class PlayerAnimHandler {

    private boolean allLoaded;
    private double scalingFactor = 1.5;

    private boolean playOnce;
    private boolean inCombat;
    private int numOfFrames;
    private long stopTime;
    private int facing;

    private SpriteSheet currentAnim;

    private SpriteSheet idleR;
    private SpriteSheet idleL;

    private SpriteSheet runR;
    private SpriteSheet runL;

    private SpriteSheet jumpR;
    private SpriteSheet jumpL;

    private SpriteSheet fallR;
    private SpriteSheet fallL;
    private SpriteSheet landR;
    private SpriteSheet landL;

    private SpriteSheet combatIdle;

    private SpriteSheet attackQ;
    private SpriteSheet attackH;

    private SpriteSheet peakR;
    private SpriteSheet peakL;


    public PlayerAnimHandler() {
        try {
            idleL = new SpriteSheet(new Image("Adventurer animations/Adventurer idleL.png"),
                    4, 1, 4, 50, 37, 8, scalingFactor);

            idleR = new SpriteSheet(new Image("Adventurer animations/Adventurer idle.png"),
                    4, 1, 4, 50, 37, 8, scalingFactor);

            runR = new SpriteSheet(new Image("Adventurer animations/Adventurer run.png"),
                    6, 1, 6, 50, 37, 6, scalingFactor);

            runL = new SpriteSheet(new Image("Adventurer animations/Adventurer runL.png"),
                    6, 1, 6, 50, 37, 6, scalingFactor);

            jumpR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRiseR.png"),
                    5, 1, 5, 50, 37, 2, scalingFactor);

            jumpL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRiseL.png"),
                    5, 1, 5, 50, 37, 2, scalingFactor);

            fallL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpFallL.png"),
                    2, 1, 2, 50, 37, 4, scalingFactor);

            fallR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpFallR.png"),
                    2, 1, 2, 50, 37, 4, scalingFactor);

            landL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRecL.png"),
                    2, 1, 2, 50, 37, 6, scalingFactor);

            landR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRecR.png"),
                    2, 1, 2, 50, 37, 6, scalingFactor);

            combatIdle = new SpriteSheet(new Image("Adventurer animations/Adventurer Combat Idle.png"),
                    4, 1, 4, 50, 37, 10, scalingFactor);

            attackH = new SpriteSheet(new Image("Adventurer animations/Adventurer H slash.png"),
                    5, 1, 5, 50, 37, 6, scalingFactor);

            attackQ = new SpriteSheet(new Image("Adventurer animations/Adventurer Q Slash.png"),
                    6, 1, 6, 50, 37, 6, scalingFactor);

            peakL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpPeakL.png"),
                    1, 1, 1, 50, 37, 1, scalingFactor);
            peakR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpPeakR.png"),
                    1, 1, 1, 50, 37, 1, scalingFactor);

            allLoaded = true;
        } catch (Exception e) {
            allLoaded = false;
        }
        if (allLoaded) {
            currentAnim = idleR;
            currentAnim.resetAnim();
        }
    }

    public boolean isAllLoaded() {
        return allLoaded;
    }

    /**
     * Draws current animation on given position. Also handles every animation that is supposed to be played only once
     *
     * @param gc  Graphics context of canvas
     * @param pos Position where to draw current sprite of current animation
     */
    public void drawAnim(GraphicsContext gc, Point2D pos) {

        if (!allLoaded) {
            return;
        }

        if(playOnce) {
            if(numOfFrames == 0) {
                currentAnim.resetAnim();
                if(inCombat) {
                    pIdle(0);
                } else {
                    pIdle(facing);
                }
            } else {
                numOfFrames--;
            }
        }
        currentAnim.nextFrame();
        currentAnim.drawSprite(gc, pos);
    }

    /**
     * Plays running animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pRun(int dir) {
        if (dir < 0) {
            if (runL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (runR.equals(currentAnim)) {
                return;
            }
        }
        if (dir < 0) {
            currentAnim = runL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = runR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    /**
     * Plays idle animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pIdle(int dir) {
        if (dir < 0) {
            if (idleL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (idleR.equals(currentAnim)) {
                return;
            }
        }
        if (dir == 0) {
            if (combatIdle.equals(currentAnim)) {
                return;
            }
        }
        playOnce = false;
        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = idleL;
        }
        if (dir > 0) {
            currentAnim = idleR;
        }
        if (dir == 0) {
            currentAnim = combatIdle;
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    /**
     * Plays jump animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pJump(int dir) {
        if (dir < 0) {
            if (jumpL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (jumpR.equals(currentAnim)) {
                return;
            }
        }

        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = jumpL;
            playOnce = true;
            inCombat = false;
            numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();
        }
        if (dir > 0) {
            currentAnim = jumpR;
            playOnce = true;
            inCombat = false;
            numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    /**
     * Plays falling animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pFall(int dir) {
        if (dir < 0) {
            if (fallL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (fallR.equals(currentAnim)) {
                return;
            }
        }

        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = fallL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = fallR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    /**
     * Plays jump peak animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pPeak(int dir) {
        if (dir < 0) {
            if (peakL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (peakR.equals(currentAnim)) {
                return;
            }
        }
        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = peakL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = peakR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.resetAnim();
    }

    /**
     * Plays landing animation in the right (as in correct) direction
     *
     * @param dir Direction of the animation, 1 if facing right, -1 if facing right
     */
    public void pLand(int dir) {
        if (dir < 0) {
            if (landL.equals(currentAnim)) {
                return;
            }
        }
        if (dir > 0) {
            if (landR.equals(currentAnim)) {
                return;
            }
        }

        currentAnim.resetAnim();
        if (dir < 0) {
            currentAnim = landL;
            playOnce = true;
            inCombat = false;
            numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();
        }
        if (dir > 0) {
            currentAnim = landR;
            playOnce = true;
            inCombat = false;
            numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();
        }
        currentAnim.resetAnim();
    }

    /**
     * Plays quick attack animation
     */
    public void pAttackQ() {
        if (attackQ.equals(currentAnim)) {
            return;
        }

        currentAnim.resetAnim();
        currentAnim = attackQ;

        playOnce = true;
        inCombat = true;
        numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();

        currentAnim.resetAnim();
    }

    /**
     * Plays hard attack animation
     */
    public void pAttackH() {
        if (attackH.equals(currentAnim)) {
            return;
        }

        currentAnim.resetAnim();
        currentAnim = attackH;

        playOnce = true;
        inCombat = true;
        numOfFrames = currentAnim.getTotalFrames() * currentAnim.getWaitFrames();

        currentAnim.resetAnim();
    }

    /**
     * Huge setter for scaling factor
     */
    public void setScalingFactor(double scalingFactor) {
        idleR.setScalingFactor(scalingFactor);
        idleL.setScalingFactor(scalingFactor);

        runR.setScalingFactor(scalingFactor);
        runL.setScalingFactor(scalingFactor);

        jumpR.setScalingFactor(scalingFactor);
        jumpL.setScalingFactor(scalingFactor);

        fallR.setScalingFactor(scalingFactor);
        fallL.setScalingFactor(scalingFactor);
        landR.setScalingFactor(scalingFactor);
        landL.setScalingFactor(scalingFactor);

        combatIdle.setScalingFactor(scalingFactor);

        attackQ.setScalingFactor(scalingFactor);
        attackH.setScalingFactor(scalingFactor);

        peakR.setScalingFactor(scalingFactor);
        peakL.setScalingFactor(scalingFactor);
    }
}
