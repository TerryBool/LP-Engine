package cz.fel.cvut.pletirad.game.enemies.wraith;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Extremely lethal enemy with multiple moves but is stubborn enough to only use one
 */

public class Wraith extends Enemy {

    private WraithAnimHandler ah = null;
    private final int HEIGHT = 100;
    private final int WIDTH = 40;
    private final double gravity = .2;
    private double speed = .5;

    private int boundLeft;
    private int boundRight;

    private double velX;
    private double velY;

    private Move spook;
    private Move lifesteal;
    private boolean loaded;
    
    private boolean playedDeath = false;
    
    public Wraith() {
        setMaxHP(120);
        setHealth(120);
        setMaxMP(30);
        setMana(30);

        boundLeft = -900;
        boundRight = -1100;
        this.pos = new Vector(-1000, -1000);
        setLayer(Layers.ENEMY);
        spook = new WraithSpook();
        lifesteal = new WraithDrain();
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

    public Wraith(int boundL, int boundR, int posX, int posY, CollisionDetector cd) {
        this.cd = cd;

        setMaxHP(120);
        setHealth(120);
        setMaxMP(30);
        setMana(30);

        boundLeft = boundL;
        boundRight = boundR;
        this.pos = new Vector(posX, posY);
        setLayer(Layers.ENEMY);
        spook = new WraithSpook();
        lifesteal = new WraithDrain();
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
            ah = new WraithAnimHandler();
            if(!ah.isAllLoaded()) {
                killObject();
                return;
            }
        }
        Vector position = (Vector) pos.subtract(cameraOffset);
        ah.drawAnim(gc, position);
    }

    @Override
    public void onCollision(GameObject go) {
    }


    public void updateHitBox() {
        hitBox = new HitBox(pos.getX() + 18, pos.getY() + 4, WIDTH, HEIGHT);
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
        return null;
    }

    @Override
    public String fetchVictoryText() {
        return "You have defeated Wraith, its body,\n however dropped absolutely nothing";
    }

    @Override
    public Move fetchMove() {
        if(ah != null) {
            ah.pAttack();
        }
        if (getHealth() <= 60 && getMana() > 0 ) {
            setMana(getMana()-15);
            return lifesteal;
        }
        return spook;
    }

    @Override
    public void tbRender(GraphicsContext gc) {
        if(ah == null) {
            ah = new WraithAnimHandler();
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
        ah.drawAnim(gc, new Point2D(500, 150));
    }

}
