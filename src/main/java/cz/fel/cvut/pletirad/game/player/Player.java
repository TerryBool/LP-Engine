package cz.fel.cvut.pletirad.game.player;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Interactable;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.PickUps;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import cz.fel.cvut.pletirad.engine.logic.GameState;
import cz.fel.cvut.pletirad.engine.logic.GameStateManager;
import cz.fel.cvut.pletirad.engine.logic.TurnBasedManager;
import cz.fel.cvut.pletirad.game.player.moves.HeavyAttack;
import cz.fel.cvut.pletirad.game.player.moves.QuickAttack;
import cz.fel.cvut.pletirad.game.testobjects.Potion;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    // Bigger collections
    private InputHandler ih;
    private CollisionDetector cd;
    private final PlayerAnimHandler ah;
    private PlayerMoveMenu pmm;

    // Physics, movement and help for rendering
    private final int HEIGHT = 48;
    private final int WIDTH = 39;

    private double velX = 0;
    private double maxSpeed = 4;
    private int endingLag;
    private int facing;
    private boolean jumping = true;

    private double velY = 0;
    private double maxFallSpeed = 5;
    private final double gravity = .2;

    // Player logic and properties
    private int maxHP = 100;
    private int maxMP = 100;
    private int health = 100;
    private int mp = 100;
    private int xp = 0;

    private List<Move> attackList;
    private List<Item> itemList;
    private List<Move> magicList;

    public Player() {
        ah = new PlayerAnimHandler();
        setLayer(Layers.PLAYER);

        pos = new Vector(350, 180);
        setPos(pos);
        updateHitBox();

        attackList = new ArrayList<>();
        itemList = new ArrayList<>();
        magicList = new ArrayList<>();

        itemList.add(new Potion());
        attackList.add(new QuickAttack());
        attackList.add(new HeavyAttack());

        if (!ah.isAllLoaded()) {
            killObject();
        }
        ah.pIdle(1);
    }

    public void initPlayer(InputHandler ih, CollisionDetector cd) {
        this.ih = ih;
        this.cd = cd;
    }

    @Override
    public void update() {
        if (ih != null && cd != null) {
            move();
        }
        updateHitBox();
    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Vector camPosition = pos.subtract(cameraOffset);
        Vector hitBoxPos = new Vector(hitBox.getMinX(), hitBox.getMinY());
        hitBoxPos = hitBoxPos.subtract(cameraOffset);
        gc.setFill(Color.GREEN);
        //gc.fillRect(hitBoxPos.getX(), hitBoxPos.getY(), hitBox.getWidth(), hitBox.getHeight());
        ah.drawAnim(gc, camPosition);
    }

    public void tbRender(GraphicsContext gc) {
        ah.drawAnim(gc, new Vector(100, 170));
        pmm.render(gc);
    }

    public Move getMove(Point2D mouseInput) {
        Move move = pmm.onClick(mouseInput);
        return move;
    }

    @Override
    public void onCollision(GameObject go) {
        Layers objectLayer = go.getLayer();
        if (objectLayer == Layers.ENEMY && !go.getDestroy()) {
            pmm = new PlayerMoveMenu(this);
            GameStateManager.getGSM().setTBM(new TurnBasedManager(ih, this, (Enemy) go));
            GameStateManager.getGSM().setGameState(GameState.COMBAT);
            ah.pIdle(0);
            ah.setScalingFactor(2);
        }
        if (objectLayer == Layers.PICKUP) {
            if (ih.isDown(KeyCode.E)) {
                PickUps pickUp = (PickUps) go;
                itemList.add(pickUp.pickUp());
            }
        }
        if (objectLayer == Layers.INTERACTABLE) {
            Interactable interactable = (Interactable) go;
            if (ih.isDown(KeyCode.E)) {
                interactable.interact(this);
            }
        }
    }

    public void updateHitBox() {
        hitBox = new HitBox(pos.getX() + 18, pos.getY() + 8, WIDTH, HEIGHT);
    }

    private void move() {
        boolean grounded = isGrounded();
        boolean movingSide = false;

        if (grounded) {
            if (jumping) {
                endingLag = 12;
            }
            velX -= .2 * velX;
            velY = 0;
            jumping = false;
        } else {
            jumping = true;
            velX -= .1 * velX;
            velY += gravity;
        }
        if (velY > maxFallSpeed) {
            velY = maxFallSpeed;
        }


        if (grounded) {
            if (ih.isDown(KeyCode.W)) {
                ah.pJump(facing);
                jumping = true;
                endingLag = 10;
                velY = -6.5;
            }
        }

        if (ih.isDown(KeyCode.A) && endingLag < 8) {
            velX -= 1;
            movingSide = true;
        }
        if (ih.isDown(KeyCode.D) && endingLag < 8) {
            velX += 1;
            movingSide = true;
        }
        // My simulated drag is unable to actually stop the player from moving.
        // So this stops him if he slows down TOO much
        if (Math.abs(velX) < .001) {
            velX = 0;
        }

        // Stop sir! You have reached speed limit
        if (velX >= maxSpeed || velX <= -maxSpeed) {
            velX = (velX > 0) ? maxSpeed : -maxSpeed;
        }


        // Checks for clipping in walls
        Vector cast = new Vector(velX, velY);
        Vector move = cd.projectionCast(getHitBox(), cast);
        if (velX != 0) {
            facing = velX > 0 ? 1 : -1;
        }
        velX = move.getX();
        velY = move.getY();

        handleAnim(grounded, movingSide);
        endingLag = endingLag == 0 ? endingLag : --endingLag;
        pos = pos.add(move);
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

    private void handleAnim(boolean grounded, boolean movingSide) {
        if (endingLag > 0) {
            ah.pLand(facing);
            return;
        }
        if (movingSide && grounded && !jumping) {
            ah.pRun(facing);
        }
        if (!movingSide && grounded) {
            ah.pIdle(facing);
        }
        if (jumping && velY < 0 && endingLag == 0) {
            ah.pPeak(facing);
        }
        if (jumping && velY > 0) {
            ah.pFall(facing);
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMp() {
        return mp;
    }

    public int getXp() {
        return xp;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public void receiveDamage(int damage) {
        health -= damage;
    }

    public void heal(int amount) {
        health += amount;
        health = (health > maxHP) ? maxHP : health;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public List<Move> getAttackList() {
        return attackList;
    }

    public List<Move> getMagicList() {
        return magicList;
    }

    public void combatEnd() {
        ah.setScalingFactor(1.5);
    }
}
