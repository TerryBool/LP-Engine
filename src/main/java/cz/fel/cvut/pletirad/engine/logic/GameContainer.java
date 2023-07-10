package cz.fel.cvut.pletirad.engine.logic;

import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import javafx.stage.Stage;

import java.util.Iterator;

public class GameContainer {

    private GameObjectManager gom;
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

        lm.generateLevel();
        //gom = lm.loadTestingLevel();

        if (gom.getObjectList().isEmpty() || gom.getPlayer() == null) {
            stage.close();
        }
        lm.saveTestingLevel();

    }

    public void update() {
        switch (gsm.getGameState()) {
            case PLATFORMER:
                updatePlatformer();
                break;
            case COMBAT:
                gsm.getTBM().update();
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

    private void updatePlatformer() {
        Iterator<GameObject> gomIterator = gom.getObjectList().iterator();
        while (gomIterator.hasNext()) {
            GameObject go = gomIterator.next();
            go.update();
            cd.checkCollisions(go);
            if (go.getDestroy()) {
                gomIterator.remove();
            }
        }
    }
}
