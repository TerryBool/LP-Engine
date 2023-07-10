package cz.fel.cvut.pletirad.game.enemies.skeleton;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Skeletons one and only move, he ain't the most dexterous fella
 */

public class SkeletonSwing extends Move {

    public SkeletonSwing() {
        setDamageValue(35);
    }

    @Override
    public String fetchMoveText() {
        return "Skeleton used swing, be careful\nDamage dealt: " + getDamageValue();
    }

    @Override
    public void useMove() {

    }

}
