package cz.fel.cvut.pletirad.engine.gameobjects.gotypes;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.game.player.Player;

public abstract class Interactable extends GameObject {

    protected HitBox interactionRange;

    public HitBox getInteractionRange() {
        return new HitBox(interactionRange.getMinX(), interactionRange.getMinY(),
                interactionRange.getWidth(), interactionRange.getHeight());
    }

    public abstract void interact(Player player);
}
