package cz.fel.cvut.pletirad.game.testobjects;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.PickUps;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class KeyObject extends PickUps {

    private Image sprite;

    private final int WIDTH = 42;
    private final int HEIGHT = 42;

    public KeyObject() {
        setLayer(Layers.PICKUP);
        pos = new Vector(-1000, -1000);
        updateHitBox();
        setItem(new Key());
        try {
            sprite = new Image("Environment/Key.png");
        } catch (Exception e) {
            e.printStackTrace();
            killObject();
        }
    }

    public KeyObject(int posX, int posY) {
        setLayer(Layers.PICKUP);
        pos = new Vector(posX, posY);
        updateHitBox();
        setItem(new Key());
        try {
            sprite = new Image("Environment/Key.png");
        } catch (Exception e) {
            e.printStackTrace();
            killObject();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Point2D position = pos.subtract(cameraOffset);
        gc.drawImage(sprite, 0, 0, 64, 64, position.getX(), position.getY(), 42, 42);
    }

    @Override
    public void onCollision(GameObject go) {

    }

    public void updateHitBox() {
        hitBox = new HitBox(pos.getX(), pos.getY(), WIDTH, HEIGHT);
        setPos(pos);
        setHitBox(hitBox);
    }

    @Override
    public Item pickUp() {
        killObject();
        return getItem();
    }
}
