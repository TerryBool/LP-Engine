package cz.fel.cvut.pletirad.engine.gameobjects;


public abstract class Move {

    // Player move code should start with 2
    // Enemy move code should start with 4
    protected int moveCode;
    private int damageValue = 0;
    private int healingValue = 0;
    private int defenseBuff = 0;
    private String moveName;

    protected Move() {
        moveName = null;
        moveCode = 0;
    }

    protected Move(int damageValue, int healingValue, int defenseBuff) {
        this.damageValue = damageValue;
        this.healingValue = healingValue;
        this.defenseBuff = defenseBuff;
    }

    public abstract String fetchMoveText();

    public abstract void useMove();

    protected void setMoveName(String name) {
        moveName = name;
    }

    public String getMoveName() {
        return moveName;
    }

    public int getMoveCode() {
        return moveCode;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public int getHealingValue() {
        return healingValue;
    }

    public void setHealingValue(int healingValue) {
        this.healingValue = healingValue;
    }

    public int getDefenseBuff() {
        return defenseBuff;
    }

    public void setDefenseBuff(int defenseBuff) {
        this.defenseBuff = defenseBuff;
    }
}
