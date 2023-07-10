package cz.fel.cvut.pletirad.game.enemies.wraith;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import cz.fel.cvut.pletirad.game.enemies.skeleton.SkeletonSwing;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;


public class Wraith extends Enemy {

    private WraithAnimHandler ah;
    private CollisionDetector cd;
    private final int HEIGHT = 70;
    private final int WIDTH = 40;
    private final double gravity = .2;
    private double speed = .5;

    @JsonInclude
    private int boundLeft;
    @JsonInclude
    private int boundRight;

    private double velX;
    private double velY;

    private Move spit;
    private boolean loaded;

    public Wraith() {
        ah = new WraithAnimHandler();

        setMaxHP(80);
        setHealth(80);

        setMana(30);

        if (!ah.isAllLoaded()) {
            System.out.println("Error with loading");
            killObject();
        }
        boundLeft = -900;
        boundRight = -1100;
        this.pos = new Vector(-1000, -1000);
        setLayer(Layers.ENEMY);
        spit = new SkeletonSwing();
        updateHitBox();
        loaded = true;
    }

    public Wraith(int boundL, int boundR, int posX, int posY, CollisionDetector cd) {
        this.cd = cd;
        ah = new WraithAnimHandler();

        setMaxHP(80);
        setHealth(80);

        setMana(30);

        if (!ah.isAllLoaded()) {
            System.out.println("Error with loading");
            killObject();
        }
        boundLeft = boundL;
        boundRight = boundR;
        this.pos = new Vector(posX, posY);
        setLayer(Layers.ENEMY);
        spit = new WraithSpook();
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
        Vector position = (Vector) pos.subtract(cameraOffset);
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
    public Move fetchMove() {
        return spit;
    }

    @Override
    public void tbRender(GraphicsContext gc) {
        ah.setScalingFactor(2);
        ah.drawAnim(gc, new Point2D(500, 150));
    }

}
