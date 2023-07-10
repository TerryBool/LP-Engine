package cz.fel.cvut.pletirad.game.enemies.skeleton;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Skeleton extends Enemy {

    private SkeletonAnimHandler ah;
    private final int HEIGHT = 55;
    private final int WIDTH = 40;
    private final double gravity = .2;
    private double speed = .5;

    @JsonInclude
    private int boundLeft;
    @JsonInclude
    private int boundRight;

    private boolean loaded = false;

    private double velX;
    private double velY;

    private Move spit;

    public Skeleton(int boundL, int boundR, int posX, int posY, CollisionDetector cd) {
        this.cd = cd;
        ah = new SkeletonAnimHandler();

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
        spit = new SkeletonSwing();
        updateHitBox();
    }

    public Skeleton() {
        ah = new SkeletonAnimHandler();

        setMaxHP(80);
        setHealth(80);

        setMana(30);

        if (!ah.isAllLoaded()) {
            System.out.println("Error with loading");
            killObject();
        }
        boundLeft = 0;
        boundRight = 0;
        this.pos = new Vector(-1000, -1000);
        setLayer(Layers.ENEMY);
        spit = new SkeletonSwing();
        loaded = true;
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
        ah.setFacing(facing);

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
        facing = velX > 0 ? 1 : -1;
        ah.pMove(facing);
        updateHitBox();
    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Vector position = pos.subtract(cameraOffset);
        gc.setFill(Color.GREEN);
        Vector hitBoxPos = new Vector(hitBox.getMinX(), hitBox.getMinY());
        hitBoxPos = hitBoxPos.subtract(cameraOffset);
        //gc.fillRect(hitBoxPos.getX(), hitBoxPos.getY(), hitBox.getWidth(), hitBox.getHeight());
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
    public Move fetchMove() {
        return spit;
    }

    @Override
    public void tbRender(GraphicsContext gc) {
        ah.setScalingFactor(2);
        ah.drawAnim(gc, new Vector(500, 150));
    }

}
