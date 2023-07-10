package cz.fel.cvut.pletirad.game.player;


import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Move;
import cz.fel.cvut.pletirad.engine.inputs.MenuButton;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PlayerMoveMenu {
    private Rectangle2D mainBar;
    List<MenuButton> mainBarButtons;
    List<MenuButton> attackBarButtons;
    List<MenuButton> itemBarButtons;
    List<MenuButton> magicBarButtons;
    Player player;

    private Rectangle2D attackWin;
    private boolean activeAttackWin;

    private Rectangle2D itemWin;
    private boolean activeItemWin;

    private Rectangle2D magicWin;
    private boolean activeMagicWin;

    public PlayerMoveMenu(Player player) {
        this.player = player;
        mainBar = new Rectangle2D(180, 260, 360, 100);
        attackWin = new Rectangle2D(mainBar.getMinX() + 50, mainBar.getMinY() + 5,
                mainBar.getWidth() / 2 - 50, mainBar.getHeight() - 10);

        itemWin = new Rectangle2D(mainBar.getMinX() + 50 + mainBar.getWidth() / 2,
                mainBar.getMinY() + 5, mainBar.getWidth() / 2 - 55, mainBar.getHeight() - 10);

        magicWin = new Rectangle2D(mainBar.getMinX() + 50, mainBar.getMinY() + 5,
                mainBar.getWidth() / 2 - 50, mainBar.getHeight() - 10);

        mainBarButtons = new ArrayList<>();
        initMainBarButtons();
        initAttackBar();
        initItemWindow();
        initMagicWindow();
    }

    public void render(GraphicsContext gc) {
        mainWindow(gc);
        if (activeAttackWin) {
            attackWindow(gc);
        } else if (activeItemWin) {
            itemWindow(gc);
        } else if (activeMagicWin) {
            magicWindow(gc);
        }
    }

    public Move onClick(Point2D mousePos) {
        System.out.println(mousePos);
        Move move = null;
        if (mainBar.contains(mousePos)) {
            if (activeAttackWin) {
                move = checkAttack(mousePos);
            } else if (activeItemWin) {
                Item item = checkItems(mousePos);
                if (item != null) {
                    move = item.combatUse();
                    if (move == null) {
                        System.out.println("Item can't be used in combat");
                    }
                }
            } else if (activeMagicWin) {
                move = checkMagic(mousePos);
            } else {
                checkMain(mousePos);
            }
        } else {
            activeMagicWin = false;
            activeItemWin = false;
            activeAttackWin = false;
        }
        return move;
    }

    private void checkMain(Point2D mousePos) {
        for (MenuButton mb : mainBarButtons) {
            if (mb.contains(mousePos)) {
                System.out.println("Player clicked: " + mb.getName());
                if ("Attack".equals(mb.getName())) {
                    activeAttackWin = true;
                }
                if ("Items".equals(mb.getName())) {
                    activeItemWin = true;
                }
                if ("Magic".equals(mb.getName())) {
                    activeMagicWin = true;
                }
            }
        }
    }

    private Move checkAttack(Point2D mousePos) {
        for (MenuButton mb : attackBarButtons) {
            if (mb.contains(mousePos)) {
                System.out.println("Player clicked " + mb.getName());
                for (Move move : player.getAttackList()) {
                    if (move.getMoveName().equals(mb.getName())) {
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private Move checkMagic(Point2D mousePos) {
        for (MenuButton mb : magicBarButtons) {
            if (mb.contains(mousePos)) {
                System.out.println("Player clicked " + mb.getName());
                for (Move move : player.getMagicList()) {
                    if (move.getMoveName().equals(mb.getName())) {
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private Item checkItems(Point2D mousePos) {
        for (MenuButton mb : itemBarButtons) {
            if (mb.contains(mousePos)) {
                System.out.println("Player clicked " + mb.getName());
                for (Item item : player.getItemList()) {
                    if (item.getDisplayName().equals(mb.getName())) {
                        return item;
                    }
                }
            }
        }
        return null;
    }


    private void mainWindow(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.fillRoundRect(mainBar.getMinX(), mainBar.getMinY(), mainBar.getWidth(), mainBar.getHeight(), 30, 30);
        gc.strokeRoundRect(mainBar.getMinX(), mainBar.getMinY(), mainBar.getWidth(), mainBar.getHeight(), 30, 30);

        gc.setLineWidth(1);
        gc.setFill(Color.DARKBLUE);
        for (MenuButton mb : mainBarButtons) {
            gc.fillRoundRect(mb.getMinX(), mb.getMinY(), mb.getWidth(), mb.getHeight(), 10, 10);
            gc.strokeText(mb.getName(), mb.getMinX() + 2, mb.getMaxY() - 2);
        }
    }

    private void attackWindow(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.fillRoundRect(attackWin.getMinX(), attackWin.getMinY(), attackWin.getWidth(), attackWin.getHeight(), 30, 30);
        gc.strokeRoundRect(attackWin.getMinX(), attackWin.getMinY(), attackWin.getWidth(), attackWin.getHeight(), 30, 30);

        gc.setLineWidth(1);
        gc.setFill(Color.DARKBLUE);
        for (MenuButton mb : attackBarButtons) {
            gc.fillRoundRect(mb.getMinX(), mb.getMinY(), mb.getWidth(), mb.getHeight(), 10, 10);
            gc.strokeText(mb.getName(), mb.getMinX() + 2, mb.getMaxY() - 2);
        }
    }

    private void itemWindow(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.fillRoundRect(itemWin.getMinX(), itemWin.getMinY(), itemWin.getWidth(), itemWin.getHeight(), 30, 30);
        gc.strokeRoundRect(itemWin.getMinX(), itemWin.getMinY(), itemWin.getWidth(), itemWin.getHeight(), 30, 30);

        gc.setLineWidth(1);
        gc.setFill(Color.DARKBLUE);
        for (MenuButton mb : itemBarButtons) {
            gc.fillRoundRect(mb.getMinX(), mb.getMinY(), mb.getWidth(), mb.getHeight(), 10, 10);
            gc.strokeText(mb.getName(), mb.getMinX() + 2, mb.getMaxY() - 2);
        }
    }

    private void magicWindow(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.fillRoundRect(magicWin.getMinX(), magicWin.getMinY(), magicWin.getWidth(), magicWin.getHeight(), 30, 30);
        gc.strokeRoundRect(magicWin.getMinX(), magicWin.getMinY(), magicWin.getWidth(), magicWin.getHeight(), 30, 30);

        gc.setLineWidth(1);
        gc.setFill(Color.DARKBLUE);
        for (MenuButton mb : magicBarButtons) {
            gc.fillRoundRect(mb.getMinX(), mb.getMinY(), mb.getWidth(), mb.getHeight(), 10, 10);
            gc.strokeText(mb.getName(), mb.getMinX() + 2, mb.getMaxY() - 2);
        }
    }

    private void initMainBarButtons() {
        mainBarButtons.add(new MenuButton(mainBar.getMinX() + 8,
                mainBar.getMinY() + 8, 40, 15, "Attack"));

        mainBarButtons.add(new MenuButton(mainBar.getMinX() + mainBar.getWidth() / 2 + 8, mainBar.getMinY() + 8
                , 40, 15, "Items"));

        mainBarButtons.add(new MenuButton(mainBar.getMinX() + 8 + mainBar.getWidth() / 2,
                mainBar.getMinY() + 8 + mainBar.getHeight() / 2
                , 40, 15, "Flee"));

        mainBarButtons.add(new MenuButton(mainBar.getMinX() + 8, mainBar.getMinY() + 8 + mainBar.getHeight() / 2
                , 40, 15, "Magic"));
    }

    private void initAttackBar() {
        attackBarButtons = new ArrayList<>();
        int pos = 0;
        if (player.getAttackList() == null) {
            System.err.println("Attack list is null");
            return;
        }
        for (Move move : player.getAttackList()) {
            attackBarButtons.add(new MenuButton(attackWin.getMinX() + 8, attackWin.getMinY() + 8 + 20 * pos,
                    move.getMoveName().length() * 6.5, 15, move.getMoveName()));
            pos++;
        }
    }

    private void initItemWindow() {
        itemBarButtons = new ArrayList<>();
        int pos = 0;
        if (player.getItemList() == null) {
            System.err.println("Attack list is null");
            return;
        }
        for (Item item : player.getItemList()) {
            itemBarButtons.add(new MenuButton(itemWin.getMinX() + 8, itemWin.getMinY() + 8 + 20 * pos,
                    item.getDisplayName().length() * 6.5, 15, item.getDisplayName()));
            pos++;
        }
    }

    private void initMagicWindow() {
        magicBarButtons = new ArrayList<>();
        int pos = 0;
        if (player.getMagicList() == null) {
            System.err.println("Attack list is null");
            return;
        }
        for (Move move : player.getMagicList()) {
            magicBarButtons.add(new MenuButton(magicWin.getMinX() + 8, magicWin.getMinY() + 8 + 20 * pos,
                    move.getMoveName().length() * 6.5, 15, move.getMoveName()));
            pos++;
        }
    }
}
