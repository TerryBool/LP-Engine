package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;

public abstract class PickUps extends GameObject {

    private Item item;

    public abstract Item pickUp();

    protected void setItem(Item item) {
        this.item = item;
    }

    protected Item getItem() {
        return item;
    }
}
