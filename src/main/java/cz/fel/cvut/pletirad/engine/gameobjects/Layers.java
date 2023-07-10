package cz.fel.cvut.pletirad.engine.gameobjects;

/**
 * This enum helps with deciding how to interact with certain objects
 */

public enum Layers {
    UNDEF(0),
    GROUND(1),
    PLAYER(2),
    NPC(3),
    ENEMY(4),
    ITEM(5),
    PICKUP(6),
    INTERACTABLE(7);

    int layerCode;

    Layers(int layerCode) {
        this.layerCode = layerCode;
    }

    public int getLayerCode() {
        return layerCode;
    }
}
