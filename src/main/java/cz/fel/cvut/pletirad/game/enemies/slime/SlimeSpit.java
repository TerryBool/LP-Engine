package cz.fel.cvut.pletirad.game.enemies.slime;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Slimes move.
 */

public class SlimeSpit extends Move {

    public SlimeSpit() {
        setDamageValue(30);
    }

    @Override
    public String fetchMoveText() {
        return "Slime used spit, it wasn't very effective\nDamage dealt: " + getDamageValue();
    }

    @Override
    public void useMove() {

    }

}
