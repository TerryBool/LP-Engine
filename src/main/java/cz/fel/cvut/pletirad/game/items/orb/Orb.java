package cz.fel.cvut.pletirad.game.items.orb;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Healing item
 */

public class Orb extends Item {
    public Orb() {
        setDisplayName("Victory orb");
        id = 13;
        Move move = new Move(0, -1000, 0) {

            @Override
            public String fetchMoveText() {
                return "You dummy";
            }

            @Override
            public void useMove() {
            }
        };

        setMove(move);
    }
}
