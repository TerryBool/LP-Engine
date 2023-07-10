package cz.fel.cvut.pletirad.game.player.moves;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

public class Heal extends Move {

    public Heal() {
        setMoveName("Heal");
        setHealingValue(50);
        moveCode = 204;
    }

    @Override
    public String fetchMoveText() {
        return "Player healed himself for 50 HP";
    }

    @Override
    public void useMove() {
    }
}
