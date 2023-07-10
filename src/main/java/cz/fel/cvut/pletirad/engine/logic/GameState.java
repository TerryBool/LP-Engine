package cz.fel.cvut.pletirad.engine.logic;

public enum GameState {
    MENU(0),
    PLATFORMER(1),
    COMBAT(2),
    PAUSED(3),
    GAMEOVER(4);

    int modeNum;

    GameState(int modeNum) {
        this.modeNum = modeNum;
    }
}
