package cz.fel.cvut.pletirad.engine.logic;

import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.game.player.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.logging.*;

/**
 * Extremely unprofessional manager for turn based combat.
 * Handles renders and calls for move for enemy and player.
 */

public class TurnBasedManager {
    private Player player;
    private Enemy enemy;

    private final InputHandler inputHandler;
    private final Image background;

    /**
     * Variable that decides whose turn it is ( true -> player, false -> enemy). (turn)
     */
    private boolean turn;
    /**
     * Ready for turn decides whether to ask player for move or not. (readyForTurn)
     */
    private boolean readyForTurn;
    private boolean renderMove;
    private boolean victory = false;

    private static final Logger LOGR = Logger.getLogger("TurnBasedManager");
    
    private Move move;
    private long textStart = -1;

    public TurnBasedManager(InputHandler inputHandler, Player player, Enemy enemy) {
        this.inputHandler = inputHandler;
        background = new Image("Combat BG/environment_forestbackground.png");
        this.player = player;
        this.enemy = enemy;
        turn = true;
        readyForTurn = true;
        
        LogManager.getLogManager().reset();
        LOGR.setLevel(Level.ALL);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        LOGR.addHandler(ch);
    }

    /**
     * Tries to get player or enemy move. If their move is null it asks them again
     */
    public void update() {
        if (readyForTurn) {
            if (turn) {
                Point2D mouseInput = inputHandler.getMouseInput();
                if (mouseInput != null) {
                    move = player.getMove(mouseInput);
                    LOGR.log(Level.FINER, "Player used {0}", move);
                    if (move != null) {
                        move.useMove();
                        enemy.receiveDmg(move.getDamageValue());
                        LOGR.log(Level.FINE, "Enemy suffered {0}", move.getDamageValue());                   
                        player.heal(move.getHealingValue());
                        readyForTurn = false;
                        renderMove = true;
                    }
                }
            } else {
                move = enemy.fetchMove();
                LOGR.log(Level.FINER, "Enemy used {0}", move);
                if (move != null) {
                    move.useMove();
                    readyForTurn = false;
                    renderMove = true;
                    player.receiveDamage(move.getDamageValue());
                    LOGR.log(Level.FINE, "Player suffered {0}", move.getDamageValue());
                    enemy.receiveDmg(-move.getHealingValue());
                }
            }
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(background, 0, 0, background.getWidth(), background.getHeight(),
                0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        if (player != null) {
            player.tbRender(gc, victory);
        }
        if (enemy != null) {
            enemy.tbRender(gc);
        }

        // Player health + mana
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);

        String info = "HP: " + player.getHealth() + "/" + player.getMaxHP() +
                "\nMP: " + player.getMp() + "/" + player.getMaxMP();
        gc.fillRoundRect(20, 280, 130, 70, 15, 15);
        gc.strokeRoundRect(20, 280, 130, 70, 15, 15);

        gc.setLineWidth(1);
        gc.strokeText(info, 30, 300);

        // Enemy health + mana
        info = "HP: " + enemy.getHealth() + "/" + enemy.getMaxHP() +
                "\nMP: " + enemy.getMana() + "/" + enemy.getMaxMP();

        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);

        gc.fillRoundRect(gc.getCanvas().getWidth() - 164, 280, 130, 70, 15, 15);
        gc.strokeRoundRect(gc.getCanvas().getWidth() - 164, 280, 130, 70, 15, 15);

        gc.setLineWidth(1);
        gc.strokeText(info, gc.getCanvas().getWidth() - 154, 300);

        if (renderMove) {
            if (textStart == -1) {
                textStart = System.currentTimeMillis();
            }
            gc.setFill(Color.BLUE);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            // mainBar = new Rectangle2D(180, 260, 360, 100);
            gc.fillRoundRect(180, 260, 360, 100, 30, 30);
            gc.strokeRoundRect(180, 260, 360, 100, 30, 30);

            gc.setLineWidth(1);
            gc.strokeText(move.fetchMoveText(), 188, 280);

            if (System.currentTimeMillis() - textStart > 2000) {
                nextTurn();
            }
        }

        if (victory) {

            gc.setFill(Color.BLUE);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            // mainBar = new Rectangle2D(180, 260, 360, 100);
            gc.fillRoundRect(180, 260, 360, 100, 30, 30);
            gc.strokeRoundRect(180, 260, 360, 100, 30, 30);

            gc.setLineWidth(1);
            gc.setStroke(Color.WHITE);
            gc.strokeText(enemy.fetchVictoryText(), 188, 280);

            if (System.currentTimeMillis() - textStart > 10000) {
                player.combatEnd();
                GameStateManager.getGSM().setGameState(GameState.PLATFORMER);
            }
        }
    }

    private void nextTurn() {
        if (player.getHealth() <= 0) {
            GameStateManager.getGSM().setGameState(GameState.GAMEOVER);
        }

        if (enemy.getHealth() <= 0) {
            victory = true;
            renderMove = false;
            readyForTurn = false;
            textStart = System.currentTimeMillis();

            Item drop = enemy.fetchDrops();
            if (drop != null) {
                player.getItemList().add(drop);
            }
            return;
        }

        turn = !turn;
        renderMove = false;
        readyForTurn = true;
        textStart = -1;
    }
}
