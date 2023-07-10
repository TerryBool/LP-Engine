package cz.fel.cvut.pletirad.engine.logic;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.graphics.PauseMenu;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

import java.util.Iterator;

/**
 * Handles every logic related things. Such as updates, call for collision detection,
 * destroying objects and loading levels
 */

public class GameContainer {

    private GameObjectManager gom;
    private LevelManager lm;
    private PauseMenu pauseMenu;
    private CollisionDetector cd;
    private final InputHandler inputHandler;
    private final Stage stage;
    private GameStateManager gsm;

    private int width = 720;
    private int height = 406;
    private final String title = "LP Engine";

    public GameContainer(InputHandler inputHandler, Stage stage) {
        this.stage = stage;
        this.inputHandler = inputHandler;
        this.gom = new GameObjectManager();
        this.cd = new CollisionDetector(gom.getObjectList());

        gsm = GameStateManager.getGSM();

        // Testing stage initialization
        LevelManager lm = new LevelManager(gom, this.inputHandler, cd);
        this.lm = lm;
        pauseMenu = new PauseMenu(lm);

        //lm.generateLevel();
        gom = lm.loadTestingLevel();

        if (gom.getObjectList().isEmpty() || gom.getPlayer() == null) {
            stage.close();
        }
        //lm.saveTestingLevel();

    }

    /**
     * Main update of the game, delegates who is supposed to handle update in certain game state
     */
    public void update() {
        switch (gsm.getGameState()) {
            case PLATFORMER:
                updatePlatformer();
                inputHandler.getMouseInput();
                break;
            case COMBAT:
                gsm.getTBM().update();
                break;
            case PAUSED:
                Point2D mousePos = inputHandler.getMouseInput();
                if (mousePos != null) {
                    if (pauseMenu.onClick(mousePos)) {
                        gom = pauseMenu.getGameObjectManager();
                        if (gom == null) {
                            stage.close();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public GameObjectManager getGameObjectManager() {
        return gom;
    }

    /**
     * Calls updates on every game object and checks if they are supposed to be destroyed
     */
    private void updatePlatformer() {
        Iterator<GameObject> gomIterator = gom.getObjectList().iterator();
        while (gomIterator.hasNext()) {
            GameObject go = gomIterator.next();
            if(go.getPos().subtract(gom.getPlayer().getPos()).magnitude() < 500) {
                go.update();
            }
            if (go.getDestroy()) {
                gomIterator.remove();
            }
        }
        cd.checkCollisions(gom.getPlayer());
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
}
