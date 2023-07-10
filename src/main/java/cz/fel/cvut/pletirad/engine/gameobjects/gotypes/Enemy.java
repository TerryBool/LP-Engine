package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import javafx.scene.canvas.GraphicsContext;

public abstract class Enemy extends GameObject {

    private int maxHP;
    private int health;
    private int mana;
    private int xp;
    protected CollisionDetector cd;

    public void updateCollisionDetector(CollisionDetector cd) {
        this.cd = cd;
    }

    public abstract Move fetchMove();

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
