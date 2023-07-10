package cz.fel.cvut.pletirad.engine.logic;

public class GameStateManager {
    private static GameStateManager gameStateManager;

    private GameState gameState;
    private TurnBasedManager tbm;

    private GameStateManager() {
        gameState = GameState.PLATFORMER;
    }

    public static GameStateManager getGSM() {
        if (gameStateManager == null) {
            gameStateManager = new GameStateManager();
        }
        return gameStateManager;
    }

    public void setGameState(GameState gameState) {
        if (this.gameState == GameState.COMBAT) {
            tbm = null;
        }
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public TurnBasedManager getTBM() {
        return tbm;
    }

    public void setTBM(TurnBasedManager tbm) {
        this.tbm = tbm;
    }
}
