package cz.fel.cvut.pletirad.game.items.bomb;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Item that deals damage to enemy.
 */
public class Bomb extends Item {

    private Move explode;
    private int used = 0;

    public Bomb() {
        setDisplayName("Bomb  ");
        setID(14);
        explode = new BombExplode();
        setMove(explode);
    }

    @Override
    public Move combatUse(){
        if (used == 1) {
            setMove(null);
            setDisplayName(null);
            return null;
        }

        used = 1;
        return explode;
    }
}
