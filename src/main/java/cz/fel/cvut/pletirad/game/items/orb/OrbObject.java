package cz.fel.cvut.pletirad.game.items.orb;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Item;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.PickUps;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Pickup that gives player Item named Orb
 */

public class OrbObject extends PickUps {

    private Image sprite;

    private final int WIDTH = 42;
    private final int HEIGHT = 42;

    public OrbObject() {
        setLayer(Layers.PICKUP);
        pos = new Vector(-1000, -1000);
        updateHitBox();
        setItem(new Orb());
        try {
            sprite = new Image("Environment/Orb.png");
        } catch (Exception e) {
            e.printStackTrace();
            killObject();
        }
    }

    public OrbObject(int posX, int posY) {
        setLayer(Layers.PICKUP);
        pos = new Vector(posX, posY);
        updateHitBox();
        setItem(new Orb());
        try {
            sprite = new Image("Environment/Orb.png");
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
        Vector position = pos.subtract(cameraOffset);
        gc.drawImage(sprite, 0, 0, 64, 64, position.getX(), position.getY(), 42, 42);
    }

    @Override
    public void onCollision(GameObject go) {

    }

    public void updateHitBox() {
        hitBox = new HitBox(pos.getX(), pos.getY(), WIDTH, HEIGHT);
    }

    @Override
    public Item pickUp() {
        killObject();
        return getItem();
    }
}
