package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;

/**
 * Objects that won't block players path, but can be interacted with
 */

public abstract class PickUps extends GameObject {

    /**
     * Item that is supposed to be given to player upon interaction (item)
     */
    private Item item;

    /**
     * Function called upon interaction with player,
     * free to implement so player might need to meet certain conditions to be able to get loot
     *
     * @return Item that is supposed to be given to player
     */
    public abstract Item pickUp();

    protected void setItem(Item item) {
        this.item = item;
    }

    protected Item getItem() {
        return item;
    }
}
