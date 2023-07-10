package cz.fel.cvut.pletirad.game.items.potion;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Healing item
 */

public class Potion extends Item {
    public Potion() {
        setDisplayName("Potion");
        id = 12;
        Move move = new Move(0, 20, 0) {

            @Override
            public String fetchMoveText() {
                return "Player used potion and regained 20 HP";
            }

            @Override
            public void useMove() {
            }
        };

        setMove(move);
    }
}
