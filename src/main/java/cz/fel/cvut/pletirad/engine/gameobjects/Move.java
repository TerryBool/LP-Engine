package cz.fel.cvut.pletirad.engine.gameobjects;


/**
 * Abstract move used in turn based combat.
 */

public abstract class Move {

    /**
     * Move code is used to decide what animation should be played in combat (moveCode)
     */
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

    /**
     * Descriptive text that is supposed to inform about effect of move
     *
     * @return Text that is supposed to be displayed when performing the move.
     */
    public abstract String fetchMoveText();

    /**
     * Is called before move is used so it can have special properties
     * For example critical chance or miss chance for powerful moves
     */
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
