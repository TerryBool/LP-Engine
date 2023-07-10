package cz.fel.cvut.pletirad.game.testobjects;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Interactable;
import cz.fel.cvut.pletirad.game.items.key.Key;
import cz.fel.cvut.pletirad.game.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.List;

/**
 * Interactable object. If player has a key it gets destroyed
 */

public class Door extends Interactable {

    private Image sprite;
    private boolean render;
    private int width = 26;
    private int height = 47;
    private boolean loaded = false;
    private double msgStart = -1;

    public Door(int posX, int posY) {
        interactionRange = new HitBox(posX - 10, posY, width + 20, height);
        try {
            sprite = new Image("Environment/Door.png");
            render = true;
        } catch (Exception e) {
            render = false;
            System.out.println("Image loading failed!");
        }
        pos = new Vector(posX, posY);
        hitBox = new HitBox(posX, posY, width, height);
        setPos(pos);
        setHitBox(hitBox);
        setLayer(Layers.INTERACTABLE);
    }

    public Door() {
        interactionRange = new HitBox(-1000, -1000, width + 20, height);
        try {
            sprite = new Image("Environment/Door.png");
            render = true;
        } catch (Exception e) {
            render = false;
        }
        pos = new Vector(-1000, -1000);
        hitBox = new HitBox(-1000, -1000, 48, 16);
        setPos(pos);
        setHitBox(hitBox);
        setLayer(Layers.INTERACTABLE);
        loaded = true;
    }


    @Override
    public void interact(Player player) {
        List<Item> playerItemList = player.getItemList();
        Iterator<Item> itemIterator = playerItemList.iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (item instanceof Key) {
                itemIterator.remove();
                killObject();
                return;
            }
        }
        msgStart = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (loaded) {
            interactionRange = new HitBox(pos.getX() - 10, pos.getY(), width + 20, height);
            loaded = false;
        }
    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Vector position = pos.subtract(cameraOffset);
        if (render) {
            gc.drawImage(sprite, position.getX(), position.getY());
            if(msgStart != -1) {
                gc.setFill(Color.BLUE);
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(2);
                gc.fillRoundRect(position.getX() + 30, position.getY() -30, 80,40,5,5);
                gc.strokeRoundRect(position.getX() + 30, position.getY() -30, 80,40,5,5);
                gc.setLineWidth(1);
                gc.strokeText("You are \nmissing a key", position.getX() + 35, position.getY() - 15);
            }
            if (System.currentTimeMillis() - msgStart > 5000) {
                msgStart = -1;
            }
        }
    }

    @Override
    public void onCollision(GameObject go) {

    }
}
