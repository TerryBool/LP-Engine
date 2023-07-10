/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.pletirad.game.items.bomb;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;

/**
 * Move of Item named Bomb
 */
public class BombExplode extends Move {
    
    public BombExplode() {
        setDamageValue(80);
    }

    @Override
    public String fetchMoveText() {
        return "You throw a bomb, it exploded and dealt massive damage.\nDamage dealt: " + getDamageValue();
    }

    @Override
    public void useMove() {
    }
    
    
    
}
