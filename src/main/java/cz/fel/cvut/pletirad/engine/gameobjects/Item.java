package cz.fel.cvut.pletirad.engine.gameobjects;

/**
 * Abstract item
 * Used for drops from enemies and to aide the player
 */

public abstract class Item {

    private String displayName = null;
    protected int id = 0;
    private Layers layer = Layers.ITEM;

    /**
     * Combat move is used in Turn based combat. If it's null then the item can't be used in combat (combatMove)
     */
    private Move combatMove = null;

    protected Item() {
    }

    public String getDisplayName() {
        return displayName;
    }

    protected void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    protected void setDisplayName(String name) {
        displayName = name;
    }

    protected void setMove(Move move) {
        combatMove = move;
    }

    public Layers getLayer() {
        return layer;
    }

    public Move combatUse() {
        return combatMove;
    }

}
