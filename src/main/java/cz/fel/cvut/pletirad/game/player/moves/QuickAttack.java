package cz.fel.cvut.pletirad.game.player.moves;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Attack with low base damage, but can perform a critical strike which doubles the damage
 */
public class QuickAttack extends Move {

    int previousDamage = 30;

    public QuickAttack() {
        moveCode = 200;
        setDamageValue(25);
        setMoveName("Quick Attack");
    }

    public void increaseDamage(int damage) {
        setDamageValue(damage);
        previousDamage = damage;
    }

    @Override
    public String fetchMoveText() {
        return "Player used Quick Attack it dealt " + getDamageValue() + " damage.";
    }

    @Override
    public void useMove() {
        if (previousDamage != getDamageValue()) {
            setDamageValue(previousDamage);
        }
        // Critical strike
        if (Math.random() < 0.2) {
            previousDamage = getDamageValue();
            setDamageValue(previousDamage * 2);
        }
    }
}
