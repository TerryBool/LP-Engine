package cz.fel.cvut.pletirad.engine.graphics;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.inputs.MenuButton;
import cz.fel.cvut.pletirad.engine.logic.LevelManager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Helping class for few button.
 * Used for saving, loading and quitting game
 */

public class PauseMenu {

    private LevelManager lm;

    /**
     * Button used for saving level (saveLevel)
     */
    private MenuButton saveLevel;

    /**
     * Button used for loading level (loadLevel)
     */
    private MenuButton loadLevel;

    /**
     * Button used for quitting game (quitGame)
     */
    private MenuButton quitGame;

    /**
     * GameObjectManager that is supposed to be given to GameContainer upon loading from previous save
     */
    private GameObjectManager gameObjectManager;

    public PauseMenu(LevelManager lm) {
        this.lm = lm;
        loadLevel = new MenuButton(300, 100, 120, 40, "LOAD");
        saveLevel = new MenuButton(300, 150, 120, 40, "SAVE");
        quitGame = new MenuButton(300, 200, 120, 40, "QUIT");
    }

    /**
     * Function called upon detection of mouse input
     *
     * @param mousePos Where user has clicked on canvas
     * @return True if level was loaded or user wants to exit game, false otherwise
     */

    public boolean onClick(Point2D mousePos) {
        if (saveLevel.contains(mousePos)) {
            lm.saveCurrentState();
        }
        if (lm.hasSave() && loadLevel.contains(mousePos)) {
            gameObjectManager = lm.loadLatestSave();
            return true;
        }
        if (quitGame.contains(mousePos)) {
            gameObjectManager = null;
            return true;
        }
        return false;
    }

    /**
     * Rendering of pause menu
     *
     * @param gc Graphics context of canvas
     */
    public void renderPM(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);

        if (lm.hasSave()) {
            gc.strokeRoundRect(loadLevel.getMinX(), loadLevel.getMinY(),
                    loadLevel.getWidth(), loadLevel.getHeight(), 10, 10);

            gc.fillRoundRect(loadLevel.getMinX(), loadLevel.getMinY(),
                    loadLevel.getWidth(), loadLevel.getHeight(), 10, 10);
        }

        gc.strokeRoundRect(saveLevel.getMinX(), saveLevel.getMinY(),
                saveLevel.getWidth(), saveLevel.getHeight(), 10, 10);

        gc.fillRoundRect(saveLevel.getMinX(), saveLevel.getMinY(),
                saveLevel.getWidth(), saveLevel.getHeight(), 10, 10);

        gc.strokeRoundRect(quitGame.getMinX(), quitGame.getMinY(),
                quitGame.getWidth(), quitGame.getHeight(), 10, 10);

        gc.fillRoundRect(quitGame.getMinX(), quitGame.getMinY(),
                quitGame.getWidth(), quitGame.getHeight(), 10, 10);


        gc.setLineWidth(1.5);
        double defaultSize = gc.getFont().getSize();
        gc.setFont(new Font(16));

        if (lm.hasSave()) {
            gc.strokeText(loadLevel.getName(), loadLevel.getMinX() + 37, loadLevel.getMinY() + 25);
        }

        gc.strokeText(saveLevel.getName(), saveLevel.getMinX() + 37, saveLevel.getMinY() + 25);
        gc.strokeText(quitGame.getName(), quitGame.getMinX() + 37, quitGame.getMinY() + 25);

        gc.setFont(new Font(defaultSize));
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }
}
