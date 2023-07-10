package cz.fel.cvut.pletirad.game.items.Knife;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Item usable in combat, it deals pretty low damage
 */

public class ThrowingKnife extends Item {
    public ThrowingKnife() {
        setDisplayName("Throwing Knife");
        id = 13;
        Move move = new Move(15, 0, 0) {

            @Override
            public String fetchMoveText() {
                return "You have thrown a knife, it dealt measly 15 dmg";
            }

            @Override
            public void useMove() {
            }
        };

        setMove(move);
    }
}
