package cz.fel.cvut.pletirad.game.enemies.skeleton;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import cz.fel.cvut.pletirad.game.items.Knife.ThrowingKnife;
import javafx.scene.canvas.GraphicsContext;


/**
 * Enemy skeleton, it moves between certain boundaries.
 */

public class Skeleton extends Enemy {

    private SkeletonAnimHandler ah = null;
    private final int HEIGHT = 54;
    private final int WIDTH = 40;
    private final double gravity = .2;
    private double speed = .5;

    private boolean playedDeath = false;
    
    private int boundLeft;
    private int boundRight;

    private boolean loaded = false;

    private double velX;
    private double velY;

    private Move swing;


    public Skeleton() {
        setMaxHP(80);
        setHealth(80);

        setMana(0);

        boundLeft = -900;
        boundRight = -1100;
        this.pos = new Vector(-1000, -1000);
        setLayer(Layers.ENEMY);
        swing = new SkeletonSwing();
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

    public Skeleton(int boundL, int boundR, int posX, int posY, CollisionDetector cd) {
        this.cd = cd;

        setMaxHP(80);
        setHealth(80);
        setMaxMP(0);
        setMana(0);

        boundLeft = boundL;
        boundRight = boundR;
        this.pos = new Vector(posX, posY);
        setLayer(Layers.ENEMY);
        swing = new SkeletonSwing();
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
            ah = new SkeletonAnimHandler();
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
        hitBox = new HitBox(pos.getX() + 10, pos.getY() + 10, WIDTH, HEIGHT);
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
        return new ThrowingKnife();
    }

    @Override
    public String fetchVictoryText() {
        return "You have defeated Skeleton.\nYou also found a throwing knife in his chest plate, lucky" +
                "\n Acquired throwing knife";
    }

    @Override
    public Move fetchMove() {
        ah.pAttack();
        return swing;
    }

    @Override
    public void tbRender(GraphicsContext gc) {
        if(ah == null) {
            ah = new SkeletonAnimHandler();
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
