package cz.fel.cvut.pletirad.engine.graphics;

import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.logic.GameContainer;
import cz.fel.cvut.pletirad.engine.logic.GameStateManager;
import cz.fel.cvut.pletirad.engine.logic.TurnBasedManager;
import cz.fel.cvut.pletirad.game.player.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Handles everything that has something to do with graphics
 */

public class Window {

    private final Stage stage;
    private final GameContainer gameContainer;
    private Canvas canvas;
    private GameObjectManager gom;
    private Vector center;
    private GameStateManager gsm;
    private TurnBasedManager tbm = null;
    private Image gameOverScreen = null;

    public Window(Stage stage, GameContainer gc) {
        this.stage = stage;
        this.gameContainer = gc;
        this.gom = gc.getGameObjectManager();
        center = new Vector(gc.getWidth() / 2 - 50, gc.getHeight() / 2);
        gsm = GameStateManager.getGSM();
        try {
            gameOverScreen = new Image("Background/GameOver.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialization of the canvas
     * All props to our hero and saviour Ladislav Seredi
     */
    public void initWindow() {
        canvas = new Canvas(gameContainer.getWidth(), gameContainer.getHeight());
        Pane pane = new Pane(canvas);
        Scene scene = new Scene(pane);

        stage.setTitle(gameContainer.getTitle());
        stage.setWidth(gameContainer.getWidth());
        stage.setHeight(gameContainer.getHeight());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Called by GameLoop everytime game is supposed to render
     * Function controls what and how is supposed to be rendered
     */
    public void render() {
        gom = gameContainer.getGameObjectManager();
        if (gom == null) {
            return;
        }

        switch (gsm.getGameState()) {
            case PLATFORMER:
                if (tbm != null) {
                    tbm = null;
                }
                renderPlatformer();
                break;
            case PAUSED:
                renderPlatformer();
                renderPauseMenu();
                break;
            case COMBAT:
                if (tbm == null) {
                    tbm = gsm.getTBM();
                }
                tbm.render(canvas.getGraphicsContext2D());
                break;
            case GAMEOVER:
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.fillRect(0, 0, gameContainer.getWidth(), gameContainer.getHeight());
                if (gameOverScreen != null) {
                    gc.drawImage(gameOverScreen, 0, 0, gameOverScreen.getWidth(), gameOverScreen.getHeight(),
                            0, 0, gameContainer.getWidth(), gameContainer.getHeight());
                }
                break;
        }
    }

    /**
     * Rendering in platformer mode of the game
     */
    private void renderPlatformer() {
        Player player = gom.getPlayer();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREY);
        gc.fillRect(0, 0, gameContainer.getWidth(), gameContainer.getHeight());

        Vector cameraPos = gom.getPlayer().getPos().subtract(center);
        for (GameObject go : gom.getObjectList()) {
            if(go.getPos().subtract(player.getPos()).magnitude() < 500) {
                go.render(gc, cameraPos);
            }
        }


        gc.setLineWidth(1);
        gc.setStroke(Color.WHITE);
        String info = "HP: " + player.getHealth() + "/" + player.getMaxHP() +
                "\nMP: " + player.getMp() + "\nXP: " + player.getXp();

        List<Item> itemList = player.getItemList();
        String items = "";
        int y = 20;
        for (Item item : itemList) {
            gc.strokeText(item.getDisplayName(), 640, y);
            y += 14;
        }
        gc.strokeText("Items: ", 600, 20);

        gc.strokeText(info, 10, 20);
    }

    private void renderPauseMenu() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gameContainer.getPauseMenu().renderPM(gc);
    }

}
