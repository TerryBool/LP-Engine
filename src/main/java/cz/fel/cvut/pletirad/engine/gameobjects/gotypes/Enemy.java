package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import javafx.scene.canvas.GraphicsContext;


/**
 * Abstract enemy that is supposed to be used in turn based combat
 */

public abstract class Enemy extends GameObject {

    private int maxHP;
    private int maxMP;
    private int health;
    private int mana;
    

    /**
     * Amount of xp enemy is supposed to drop. Not implemented in actual combat, sadly enough (xp)
     */
    private int xp;
    protected CollisionDetector cd;

    /**
     * Used when loading through Jackson
     *
     * @param cd Collision detector with updated GameObject list
     */
    public void updateCollisionDetector(CollisionDetector cd) {
        this.cd = cd;
    }

    /**
     * Called once enemy is defeated
     *
     * @return Item that is then given to player
     */
    public abstract Item fetchDrops();

    /**
     * Informative and celebratory text. (Also used to raise players ego)
     *
     * @return Uplifting text serving to inform player about drops and events that might occur after defeat of enemy
     */
    public abstract String fetchVictoryText();

    /**
     * Function where enemy is supposed to decide what kind of move it wants to use
     *
     * @return Move that will be used in combat
     */
    public abstract Move fetchMove();

    /**
     * Render in turn based combat.
     *
     * @param gc Graphics context of canvas
     */
    public abstract void tbRender(GraphicsContext gc);

    public void receiveDmg(int amount) {
        this.health -= amount;
        if (health <= 0) {
            this.health = 0;
            killObject();
        }
    }

    public void heal(int amount) {
        health += amount;
        health = (health > maxHP) ? maxHP : health;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }

    public int getHealth() {
        return health;
    }

    protected void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    protected void setMana(int mana) {
        this.mana = mana;
    }

    protected void setXp(int xp) {
        this.xp = xp;
    }

    public int getMana() {
        return mana;
    }

    public int getXp() {
        return xp;
    }

}
