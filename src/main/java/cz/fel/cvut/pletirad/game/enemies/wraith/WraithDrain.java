package cz.fel.cvut.pletirad.game.enemies.wraith;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Self healing and damaging move used by Wraith.
 */

public class WraithDrain extends Move {

    public WraithDrain() {
        setDamageValue(20);
        setHealingValue(20);
    }

    @Override
    public String fetchMoveText() {
        return "Wraith drained you.\nDamage dealt: " + getDamageValue() + "\nHealing done: " + getHealingValue();
    }

    @Override
    public void useMove() {

    }

}
