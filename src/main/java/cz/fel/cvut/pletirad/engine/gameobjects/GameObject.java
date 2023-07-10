package cz.fel.cvut.pletirad.engine.gameobjects;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {

    protected HitBox hitBox = null;
    protected Vector pos;
    private Layers layer = Layers.UNDEF;
    private boolean destroy = false;

    /*
     *  Game object behaviour
     */
    public abstract void update();

    /*
     * Drawing game object into graphics context
     */
    public abstract void render(GraphicsContext gc, Vector cameraOffset);

    /*
     * Gets called when collision is detected
     */
    public abstract void onCollision(GameObject go);

    public boolean checkCollision(HitBox hb) {
        if (hitBox == null) {
            return false;
        }
        return hitBox.intersects(hb);
    }

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

    /*
     * Return new Point2D so they can't change GO's position
     */

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
