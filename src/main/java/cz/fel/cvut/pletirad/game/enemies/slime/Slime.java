package cz.fel.cvut.pletirad.game.enemies.slime;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import cz.fel.cvut.pletirad.game.items.potion.Potion;
import javafx.scene.canvas.GraphicsContext;


/**
 * Green blob who moves withing certain boundaries
 * Drops potion
 */

public class Slime extends Enemy {

    private SlimeAnimHandler ah = null;
    private final int HEIGHT = 70;
    private final int WIDTH = 40;
    private final double gravity = .2;
    private double speed = .5;

    private boolean playedDeath = false;

    private int boundLeft;
    private int boundRight;

    private double velX;
    private double velY;

    private Move spit;

    private boolean loaded = false;

    public Slime() {

        setMaxHP(60);
        setHealth(60);

        setMana(30);

        boundLeft = -900;
        boundRight = -1100;
        pos = new Vector(-1000, -1000);
        setLayer(Layers.ENEMY);
        spit = new SlimeSpit();
        updateHitBox();
        loaded = true;
    }

    /**
     * @param boundL Left boundary, he won't go beyond that
     * @param boundR Right boundary, he won't go beyond that
     * @param posX   Initial X position
     * @param posY   Initial Y position
     * @param cd     Collision detector
     */
    public Slime(int boundL, int boundR, int posX, int posY, CollisionDetector cd) {
        this.cd = cd;

        setMaxHP(60);
        setHealth(60);
        setMaxMP(0);
        setMana(0);

        boundLeft = boundL;
        boundRight = boundR;
        this.pos = new Vector(posX, posY);
        setLayer(Layers.ENEMY);
        spit = new SlimeSpit();
        updateHitBox();
    }

    @Override
    public void update() {
        if (loaded) {
            boundLeft = (int) Math.round(pos.getX() - 50);
            boundRight = (int) Math.round(pos.getX() + 50);
            loaded = false;
        }
        boolean grounded = isGrounded();
        int facing = velX > 0 ? 1 : -1;

        if (pos.getX() <= boundLeft && facing == -1 || pos.getX() >= boundRight && facing == 1) {
            speed *= -1;
        }

        if (grounded) {
            velX -= .2 * velX;
            velY = 0;
        } else {
            velX -= .1 * velX;
            velY += gravity;
        }
        if (velY > 5) {
            velY = 5;
        }

        velX = speed;


        Vector cast = new Vector(velX, velY);
        Vector move = cd.projectionCast(getHitBox(), cast);
        velX = move.getX();
        velY = move.getY();
        pos = pos.add(move);
        updateHitBox();
    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {

        if(ah == null) {
            ah = new SlimeAnimHandler();
            if(!ah.isAllLoaded()) {
                killObject();
                return;
            }
        }

        Vector position = pos.subtract(cameraOffset);
        ah.drawAnim(gc, position);
    }

    @Override
    public void onCollision(GameObject go) {
    }


    public void updateHitBox() {
        hitBox = new HitBox(pos.getX() + 44, pos.getY() + 10, WIDTH, HEIGHT);
    }

    private boolean isGrounded() {
        Vector down = new Vector(0, 1);
        double leftSideD = cd.rayCast(new Vector(hitBox.getMinX(), hitBox.getMaxY()), down);
        double rightSideD = cd.rayCast(new Vector(hitBox.getMaxX(), hitBox.getMaxY()), down);
        double center = (hitBox.getMaxX() - hitBox.getMinX()) / 2 + hitBox.getMinX();
        double centerDist = cd.rayCast(new Vector(center, hitBox.getMaxY()), down);

        if (leftSideD < 0.2 || rightSideD < 0.2 || centerDist < 0.2) {
            return true;
        }
        return false;
    }

    @Override
    public Item fetchDrops() {
        return new Potion();
    }

    @Override
    public String fetchVictoryText() {
        return "You have defeated slime.\nYou see potion and half corroded shoulder guard in his goo.\n" +
                "You take potion and leave the useless shoulder guard there";
    }

    @Override
    public Move fetchMove() {
        ah.pAttack();
        return spit;
    }

    @Override
    public void tbRender(GraphicsContext gc) {
        if(ah == null) {
            ah = new SlimeAnimHandler();
            if(!ah.isAllLoaded()) {
                killObject();
                return;
            }
        }
        if (!playedDeath && getHealth() <= 0) {
            ah.pDeath();
            playedDeath = true;
        }
        ah.setScalingFactor(2);
        ah.drawAnim(gc, new Vector(500, 150));
    }

}
