package cz.fel.cvut.pletirad.game.testobjects;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class DirtWall extends GameObject {

    Image sprite;
    boolean render;

    public DirtWall() {
        try {
            sprite = new Image("Environment/dirtWall.png");
            render = true;
        } catch (Exception e) {
            render = false;
            System.out.println("Image loading failed!");
        }
        pos = new Vector(-1000, -1000);
        hitBox = new HitBox(-1000, -1000, 16, 46);
        setPos(pos);
        setHitBox(hitBox);
        setLayer(Layers.GROUND);
    }

    public DirtWall(int x, int y) {
        try {
            sprite = new Image("Environment/dirtWall.png");
            render = true;
        } catch (Exception e) {
            render = false;
            System.out.println("Image loading failed!");
        }
        pos = new Vector(x, y);
        hitBox = new HitBox(x, y, 16, 46);
        setPos(pos);
        setHitBox(hitBox);
        setLayer(Layers.GROUND);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc, Vector cameraOffset) {
        Vector position = pos.subtract(cameraOffset);
        if (render) {
            gc.drawImage(sprite, position.getX(), position.getY());
        }
    }

    @Override
    public void onCollision(GameObject go) {

    }
}
