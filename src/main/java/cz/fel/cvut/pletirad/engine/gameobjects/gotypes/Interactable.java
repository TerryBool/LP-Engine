package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.game.player.Player;


/**
 * Object with player is supposed to interact with.
 * For example chests and doors
 */
public abstract class Interactable extends GameObject {

    /**
     * Area around an object where player can interact with it (interactionRange)
     */
    protected HitBox interactionRange;

    public HitBox getInteractionRange() {
        return new HitBox(interactionRange.getMinX(), interactionRange.getMinY(),
                interactionRange.getWidth(), interactionRange.getHeight());
    }

    /**
     * Function called when player interacts with object
     *
     * @param player Player, so object can perform changes to him
     */
    public abstract void interact(Player player);
}
