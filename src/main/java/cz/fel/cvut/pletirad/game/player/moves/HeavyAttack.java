package cz.fel.cvut.pletirad.game.player.moves;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

public class HeavyAttack extends Move {

    private boolean missed = false;
    int previousDamage = 50;

    public HeavyAttack() {
        moveCode = 201;
        setMoveName("Heavy attack");
        setDamageValue(50);
    }

    public void increaseDamage(int damage) {
        setDamageValue(damage);
        previousDamage = damage;
    }

    @Override
    public String fetchMoveText() {
        if (missed) {
            return "Player's mighty swing completely missed the target.";
        }
        return "Player used Heavy Attack it dealt " + getDamageValue() + " damage.";
    }

    @Override
    public void useMove() {

        if (previousDamage != getDamageValue()) {
            setDamageValue(previousDamage);
        }

        if (Math.random() < 0.4) {
            previousDamage = getDamageValue();
            setDamageValue(0);
            missed = true;
        }
    }
}
