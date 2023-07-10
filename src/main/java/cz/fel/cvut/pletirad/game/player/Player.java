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
import cz.fel.cvut.pletirad.game.items.potion.Potion;
import cz.fel.cvut.pletirad.game.player.moves.Heal;
import cz.fel.cvut.pletirad.game.player.moves.HeavyAttack;
import cz.fel.cvut.pletirad.game.player.moves.QuickAttack;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controllable character
 */

public class Player extends GameObject {

    // Bigger collections
    private InputHandler ih;
    private CollisionDetector cd;
    private PlayerAnimHandler ah;
    private PlayerMoveMenu pmm;

    // Physics, movement and help for rendering
    private final int HEIGHT = 48;
    private final int WIDTH = 39;

    private double velX = 0;
    private double velY = 0;
    private double maxSpeed = 4;

    /**
     * Ending lag is number of frames that player is unable to make inputs. Used after landing.
     */
    private int endingLag;

    private int facing;
    private boolean jumping = true;

    /**
     * ID of move that is supposed to be rendered in turn based combat (renderMove)
     */
    private int renderMove = 0;

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
        magicList.add(new Heal());

        if (!ah.isAllLoaded()) {
            killObject();
        }
        ah.pIdle(1);
    }

    public Player(boolean testing) {
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
        magicList.add(new Heal());
    }

    /**
     * Called if player is loaded through Jackson
     *
     * @param ih Input handler for player
     * @param cd Collision detector
     */
    public void initPlayer(InputHandler ih, CollisionDetector cd) {
        this.ih = ih;
        this.cd = cd;
    }

    @Override
    public void update() {
        if (ih != null && cd != null) {
            move();
        }
        if (pos.getY() > 500) {
            GameStateManager.getGSM().setGameState(GameState.GAMEOVER);
        }
        // Uses potion if you press Q
        if (ih.isDown(KeyCode.Q)) {
            Iterator<Item> itemIterator = itemList.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                if (item instanceof Potion) {
                    heal(item.combatUse().getHealingValue());
                    itemIterator.remove();
                    break;
                }
            }
        }

        updateHitBox();
    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Vector camPosition = pos.subtract(cameraOffset);
        ah.drawAnim(gc, camPosition);
    }

    /**
     * Turn based rendering for player. Currently with only a few attacks
     *
     * @param gc      Graphics context of canvas
     * @param victory Info about combat state
     */
    public void tbRender(GraphicsContext gc, boolean victory) {
        if (renderMove != 0) {
            switch (renderMove) {
                case 200:
                    ah.pAttackQ();
                    endingLag = 60;
                    break;
                case 201:
                    ah.pAttackH();
                    endingLag = 60;
                    break;
                default:
                    break;
            }
            renderMove = 0;
        }
        endingLag = endingLag == 0 ? endingLag : --endingLag;
        if (endingLag > 0) {
            ah.drawAnim(gc, new Vector(450, 180));
        } else {
            ah.drawAnim(gc, new Vector(100, 170));
        }
        if (!victory) {
            pmm.render(gc);
        }
    }

    /**
     * Called by Turn based manager when it detects mouse input
     *
     * @param mouseInput Position of mouse input
     * @return Move if any was chosen, null otherwise
     */
    public Move getMove(Point2D mouseInput) {
        Move move = pmm.onClick(mouseInput);
        if (move != null) {
            renderMove = move.getMoveCode();
            if(renderMove == 204) {
                mp -= 20;
            }
        }
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

    /**
     * Function that wouldn't be needed if it weren't for JavaFX decision for making their Point2D and Rectangle2D final
     */
    public void updateHitBox() {
        hitBox = new HitBox(pos.getX() + 18, pos.getY() + 8, WIDTH, HEIGHT);
    }

    /**
     * Controls movement of player has a little simulated physics
     */
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

    /**
     * Checks from all sides of player hit box if he is extremely near the ground.
     *
     * @return True if on ground, false otherwise
     */
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

    /**
     * Called from move, handles which animation is supposed to be playing.
     *
     * @param grounded   Info whether player is grounded or not
     * @param movingSide True if player is moving sideways
     */
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
