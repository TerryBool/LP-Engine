package cz.fel.cvut.pletirad.engine.gameobjects;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract game object.
 * Everything in the game that is supposed to be interacted with has to be subclass of GameObject
 */

public abstract class GameObject {

    /**
     * Hit box of game object (hitBox)
     */
    protected HitBox hitBox = null;
    /**
     * Position of game object (pos)
     */
    protected Vector pos;

    /**
     * Layer of the game object, used to decide how to interact with object (layer)
     */
    private Layers layer = Layers.UNDEF;

    /**
     * Every frame is checked to see if program should destroy the object (destroy)
     */
    private boolean destroy = false;

    /**
     *  Update is call every frame of the game
     *  Behaviour of a game object is supposed to be described here
     */
    public abstract void update();

    /**
     * It's called every time game is supposed to render, describes how game object is supposed to show on screen
     *
     * @param cameraOffset Used to draw the object relative to camera.
     * @param gc Graphics context of Canvas
     */
    public abstract void render(GraphicsContext gc, Vector cameraOffset);

    /**
     * Gets called when collision is detected
     * @param go Is foreign object this instance of object collided with
     */
    public abstract void onCollision(GameObject go);

    /**
     * Checks if collision happened between this and foreign object
     *
     * @param hb Hit box we want to check collisions with
     * @return True if hb intersected with hit box of game object, false otherwise
     */

    public boolean checkCollision(HitBox hb) {
        if (hitBox == null) {
            return false;
        }
        return hitBox.intersects(hb);
    }

    /**
     * Checks if collision happened between our and foreign object
     *
     * @param pos Vector we want to check collisions with
     * @return True if our hit box contains pos, false otherwise
     */

    public boolean checkCollision(Vector pos) {
        if (hitBox == null) {
            return false;
        }
        return hitBox.contains(pos);
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    public void setLayer(Layers layer) {
        this.layer = layer;
    }

    public Vector getPos() {
        return new Vector(pos.getX(), pos.getY());
    }

    public Layers getLayer() {
        return layer;
    }

    public HitBox getHitBox() {
        return new HitBox(hitBox.getMinX(), hitBox.getMinY(),
                hitBox.getWidth(), hitBox.getHeight());
    }

    public void killObject() {
        destroy = true;
    }

    public boolean getDestroy() {
        return destroy;
    }
}
