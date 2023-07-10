package cz.fel.cvut.pletirad.engine;

import cz.fel.cvut.pletirad.engine.graphics.Window;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.engine.logic.GameContainer;
import cz.fel.cvut.pletirad.engine.logic.GameState;
import cz.fel.cvut.pletirad.engine.logic.GameStateManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.logging.*;


public class GameLoop extends Application {

    private Window gameWindow;
    private GameContainer gc;
    private AnimationTimer gameLoop;
    private InputHandler inputHandler;
    private boolean paused = false;
    private GameStateManager gsm;
    private static final Logger LOGR = Logger.getLogger("GameLoop");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        inputHandler = new InputHandler();
        gc = new GameContainer(inputHandler, primaryStage);
        gsm = GameStateManager.getGSM();

        gameWindow = new Window(primaryStage, gc);
        gameWindow.initWindow();
        gameWindow.render();

        LogManager.getLogManager().reset();
        LOGR.setLevel(Level.ALL);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        LOGR.addHandler(ch);
        
        gameLoop = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) { // in nanoseconds
                if (now - lastUpdate >= 33e5 ) {
                    lastUpdate = now;
                    gc.update();
                    gameWindow.render();
                }
            }
        };
        gameLoop.start();

        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            GameState previousGameState;
            @Override
            public void handle(KeyEvent event) {
                inputHandler.updateKeyPress(event.getCode());
                LOGR.log(Level.INFO, "Key pressed {0}", event.getCode());
                
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (GameStateManager.getGSM().getGameState() == GameState.GAMEOVER) {
                        primaryStage.close();
                    }

                    if (gsm.getGameState() == GameState.PLATFORMER || gsm.getGameState() == GameState.PAUSED) {
                        paused = !paused;
                        if (paused) {
                            previousGameState = gsm.getGameState();
                            gsm.setGameState(GameState.PAUSED);
                        } else {
                            gsm.setGameState(previousGameState);
                        }
                    }
                }
            }
        });

        primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                inputHandler.updateKeyRelease(event.getCode());
            }
        });

        primaryStage.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.PRIMARY) {
                    Point2D mouseInput = new Point2D(me.getX(), me.getY());
                    LOGR.log(Level.INFO, "Mouse clicked X: {0} Y: {1}", new Object[]{me.getX(), me.getY()});
                    inputHandler.bufferMouse(mouseInput);
                }
            }
        });
    }
}
