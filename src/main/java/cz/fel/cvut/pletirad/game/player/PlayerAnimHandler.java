package cz.fel.cvut.pletirad.game.player;

import cz.fel.cvut.pletirad.engine.graphics.SpriteSheet;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
    private SpriteSheet attackD;
    private SpriteSheet attackH;
    private SpriteSheet cast;

    private SpriteSheet death;

    private SpriteSheet peakR;
    private SpriteSheet peakL;


    public PlayerAnimHandler() {
        try {
            idleL = new SpriteSheet(new Image("Adventurer animations/Adventurer idleL.png"),
                    4, 1, 4, 50, 37, 8, scalingFactor);

            idleR = new SpriteSheet(new Image("Adventurer animations/Adventurer idle.png"),
                    4, 1, 4, 50, 37, 8, scalingFactor);

            runR = new SpriteSheet(new Image("Adventurer animations/Adventurer run.png"),
                    6, 1, 6, 50, 37, 10, scalingFactor);

            runL = new SpriteSheet(new Image("Adventurer animations/Adventurer runL.png"),
                    6, 1, 6, 50, 37, 10, scalingFactor);

            jumpR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRiseR.png"),
                    5, 1, 5, 50, 37, 30, scalingFactor);

            jumpL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRiseL.png"),
                    5, 1, 5, 50, 37, 30, scalingFactor);

            fallL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpFallL.png"),
                    2, 1, 2, 50, 37, 15, scalingFactor);

            fallR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpFallR.png"),
                    2, 1, 2, 50, 37, 15, scalingFactor);

            landL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRecL.png"),
                    2, 1, 2, 50, 37, 10, scalingFactor);

            landR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpRecR.png"),
                    2, 1, 2, 50, 37, 10, scalingFactor);

            combatIdle = new SpriteSheet(new Image("Adventurer animations/Adventurer Combat Idle.png"),
                    4, 1, 4, 50, 37, 10, scalingFactor);

            attackD = new SpriteSheet(new Image("Adventurer animations/Adventurer D Slash.png"),
                    12, 1, 12, 50, 37, 15, scalingFactor);

            attackH = new SpriteSheet(new Image("Adventurer animations/Adventurer H slash.png"),
                    5, 1, 5, 50, 37, 15, scalingFactor);

            attackQ = new SpriteSheet(new Image("Adventurer animations/Adventurer Q Slash.png"),
                    6, 1, 6, 50, 37, 15, scalingFactor);

            cast = new SpriteSheet(new Image("Adventurer animations/Adventurer Cast.png"),
                    3, 1, 3, 50, 37, 10, scalingFactor);

            death = new SpriteSheet(new Image("Adventurer animations/Adventurer death.png"),
                    5, 1, 5, 50, 37, 5, scalingFactor);

            peakL = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpPeakL.png"),
                    1, 1, 1, 50, 37, 1, scalingFactor);
            peakR = new SpriteSheet(new Image("Adventurer animations/Adventurer JumpPeakR.png"),
                    1, 1, 1, 50, 37, 1, scalingFactor);

            allLoaded = true;
        } catch (Exception e) {
            allLoaded = false;
            System.err.println("Animation load for Player failed!!");
            System.err.println(e.getMessage());
            System.err.println();
        }
        if (allLoaded) {
            currentAnim = idleR;
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
                    currentAnim = combatIdle;
                } else {
                    pIdle(facing);
                }
                currentAnim.start();
                playOnce = false;
            }
        }
        currentAnim.drawSprite(gc, pos);
    }

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
        currentAnim.stop();
        if (dir < 0) {
            currentAnim = runL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = runR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.start();
    }

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
        currentAnim.stop();
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
        currentAnim.start();
    }

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

        currentAnim.stop();
        if (dir < 0) {
            currentAnim = jumpL;
            playOnce = true;
            inCombat = false;
            numOfFrames = 5;
            // Current time + len of anim in ms (number of frames / fps of the animation)*1000
            stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 30;
        }
        if (dir > 0) {
            currentAnim = jumpR;
            playOnce = true;
            inCombat = false;
            numOfFrames = 5;
            // Current time + len of anim in ms (number of frames / fps of the animation)*1000
            stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 30;
        }
        facing = dir;
        currentAnim.start();
    }

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

        currentAnim.stop();
        if (dir < 0) {
            currentAnim = fallL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = fallR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.start();
    }

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
        currentAnim.stop();
        if (dir < 0) {
            currentAnim = peakL;
            playOnce = false;
        }
        if (dir > 0) {
            currentAnim = peakR;
            playOnce = false;
        }
        facing = dir;
        currentAnim.start();
    }

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

        currentAnim.stop();
        if (dir < 0) {
            currentAnim = landL;
            playOnce = true;
            inCombat = false;
            numOfFrames = 2;
            // Current time + len of anim in ms (number of frames / fps of the animation)*1000
            stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 10;
        }
        if (dir > 0) {
            currentAnim = landR;
            playOnce = true;
            inCombat = false;
            numOfFrames = 2;
            // Current time + len of anim in ms (number of frames / fps of the animation)*1000
            stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 10;
        }
        currentAnim.start();
    }

    public void pCast() {
        if (cast.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = cast;

        playOnce = true;
        numOfFrames = 3;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 10;

        currentAnim.start();
    }

    public void pAttackQ() {
        if (attackQ.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = attackQ;

        playOnce = true;
        inCombat = true;
        numOfFrames = 6;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 15;

        currentAnim.start();
    }

    public void pAttackD() {
        if (attackD.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = attackD;

        playOnce = true;
        inCombat = true;
        numOfFrames = 12;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 15;

        currentAnim.start();
    }

    public void pAttackH() {
        if (attackH.equals(currentAnim)) {
            return;
        }

        currentAnim.stop();
        currentAnim = attackH;

        playOnce = true;
        inCombat = true;
        numOfFrames = 5;
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
        inCombat = true;
        numOfFrames = 5;
        // Current time + len of anim in ms (number of frames / fps of the animation)*1000
        stopTime = System.currentTimeMillis() + (numOfFrames * 1000) / 5;

        currentAnim.start();
    }

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
        attackD.setScalingFactor(scalingFactor);
        attackH.setScalingFactor(scalingFactor);
        cast.setScalingFactor(scalingFactor);

        death.setScalingFactor(scalingFactor);

        peakR.setScalingFactor(scalingFactor);
        peakL.setScalingFactor(scalingFactor);
    }
}
