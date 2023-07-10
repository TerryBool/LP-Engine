package cz.fel.cvut.pletirad.game.enemies.wraith;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

public class WraithSpook extends Move {

    public WraithSpook() {
        setDamageValue(40);
    }

    @Override
    public String fetchMoveText() {
        return "Wraith spooked, it doesn't want you to continue this adventure\nDamage dealt: " + getDamageValue();
    }

    @Override
    public void useMove() {

    }

}
