package cz.fel.cvut.pletirad.game.testobjects;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;

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
