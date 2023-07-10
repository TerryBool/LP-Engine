package cz.fel.cvut.pletirad.engine.logic;

import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.game.player.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class TurnBasedManager {
    private Player player;
    private Enemy enemy;

    private final InputHandler inputHandler;
    private final Image background;

    // True -> player, false -> enemy
    private boolean turn;
    private boolean readyForTurn;
    private boolean renderMove;
    private Move move;
    private long textStart = -1;

    public TurnBasedManager(InputHandler inputHandler, Player player, Enemy enemy) {
        this.inputHandler = inputHandler;
        background = new Image("Combat BG/environment_forestbackground.png");
        this.player = player;
        this.enemy = enemy;
        turn = true;
        readyForTurn = true;
    }

    public void update() {
        if (readyForTurn) {
            if (turn) {
                Point2D mouseInput = inputHandler.getMouseInput();
                if (mouseInput != null) {
                    move = player.getMove(mouseInput);
                    if (move != null) {
                        enemy.receiveDmg(move.getDamageValue());
                        player.heal(move.getHealingValue());
                        readyForTurn = false;
                        renderMove = true;
                    }
                }
            } else {
                move = enemy.fetchMove();
                if (move != null) {
                    readyForTurn = false;
                    renderMove = true;
                    player.receiveDamage(move.getDamageValue());
                    enemy.receiveDmg(-move.getHealingValue());
                }
            }
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(background, 0, 0, background.getWidth(), background.getHeight(),
                0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        if (player != null) {
            player.tbRender(gc);
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
        info = "HP: " + enemy.getHealth() + "/" + enemy.getMaxHP();

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

            if (System.currentTimeMillis() - textStart > 3000) {
                nextTurn();
            }
        }
    }

    private void nextTurn() {
        if (player.getHealth() <= 0) {
            GameStateManager.getGSM().setGameState(GameState.GAMEOVER);
        }
        if (enemy.getHealth() <= 0) {
            player.combatEnd();
            GameStateManager.getGSM().setGameState(GameState.PLATFORMER);
        }

        turn = !turn;
        renderMove = false;
        readyForTurn = true;
        textStart = -1;
    }
}
