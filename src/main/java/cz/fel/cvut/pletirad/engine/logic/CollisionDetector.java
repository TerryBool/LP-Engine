package cz.fel.cvut.pletirad.engine.logic;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Interactable;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles every collision in the game
 */

public class CollisionDetector {

    /**
     * List of objects in the game, received from GameObjectManager (gameObjects)
     */
    ArrayList<GameObject> gameObjects;

    public CollisionDetector(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Checks every collision with gameObject and calls gameObject.onCollision when it occurs
     * <p>
     * Ground objects are ignored when checking for collisions (no interaction is supposed to happen with them)
     * Interactable objects use interaction range for collision instead of hit box
     * Enemies use hit boxes for collision detection
     * </p>
     *
     * @param gameObject Game object for which collisions will be checked
     */
    public void checkCollisions(GameObject gameObject) {
        HitBox goHB = gameObject.getHitBox();
        if (goHB == null || gameObject.getLayer() == Layers.GROUND) {
            return;
        }
        for (GameObject go : gameObjects) {

            if (go.equals(gameObject)) {
                continue;
            }
            // No possible interaction with ground.
            if (go.getLayer() == Layers.GROUND) {
                continue;
            }

            // Checking if its interactable object, since it needs different approach from enemies and such
            if (go.getLayer() == Layers.INTERACTABLE) {
                Interactable interactable = (Interactable) go;
                if (interactable.getInteractionRange().intersects(goHB)) {
                    gameObject.onCollision(go);
                    continue;
                }
            }
            // The rest of the cast. Enemies, pickups (NPCs maybe)
            if (go.checkCollision(goHB)) {
                gameObject.onCollision(go);
            }
        }
    }

    public void updateObjectList(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Creates temporary hit box and tries to get him as far as possible for every direction
     *
     * @param hitBox Initial hit box which is used for casting
     * @param dest   Final destination of hit box
     * @return Vector with maximum distance hit box can move without colliding
     */

    public Vector projectionCast(HitBox hitBox, Vector dest) {
        double scalar = 1;
        double minScalar = 1;

        double height = hitBox.getHeight();
        double width = hitBox.getWidth();
        double x = hitBox.getMinX();
        double y = hitBox.getMinY();

        HitBox projection;
        Vector p;

        List<GameObject> collisions = new ArrayList<>();

        for (GameObject go : gameObjects) {
            if ((go.getLayer() != Layers.GROUND && go.getLayer() != Layers.INTERACTABLE) || go.getHitBox() == null) {
                continue;
            }

            p = dest.multiply(scalar);
            projection = new HitBox(p.getX() + x, p.getY() + y, width, height);
            boolean added = false;
            while (go.checkCollision(projection)) {
                if (!added) {
                    added = true;
                    collisions.add(go);
                }
                scalar -= .01;
                p = dest.multiply(scalar);
                projection = new HitBox(p.getX() + x, p.getY() + y, width, height);
            }
            if (scalar < minScalar) {
                minScalar = scalar;
            }
            scalar = 1;
        }

        if (collisions.isEmpty()) {
            return dest.multiply(minScalar);
        }

        x = hitBox.getMinX() + dest.multiply(minScalar).getX();
        y = hitBox.getMinY() + dest.multiply(minScalar).getY();

        // Get maximum horizontal movement you can do without colliding
        Vector h = new Vector(dest.getX(), 0);
        double horizontal = 1;
        scalar = minScalar;

        for (GameObject go : collisions) {
            p = h.multiply(scalar);
            projection = new HitBox(x + p.getX(), y, width, height);
            while (!go.checkCollision(projection)) {
                scalar += .01;
                p = h.multiply(scalar);
                projection = new HitBox(x + p.getX(), y, width, height);
                if (scalar >= 1.01) {
                    break;
                }
            }
            horizontal = Math.min(horizontal, scalar - 0.01);
            scalar = minScalar;
        }
        h = h.multiply(horizontal);

        // Get maximum vertical movement you can do without colliding
        Vector v = new Vector(0, dest.getY());
        double vertical = 1;
        scalar = minScalar;

        for (GameObject go : collisions) {
            p = v.multiply(scalar);
            projection = new HitBox(x, p.getY() + y, width, height);
            while (!go.checkCollision(projection)) {
                scalar += .01;
                p = v.multiply(scalar);
                projection = new HitBox(x, p.getY() + y, width, height);
                if (scalar >= 1.01) {
                    break;
                }
            }
            vertical = Math.min(vertical, scalar - 0.01);
            scalar = minScalar;
        }
        v = v.multiply(vertical);

        collisions.clear();
        return v.add(h);
    }

    /**
     * Finds nearest ground object from certain position in given direction
     *
     * @param pos       Position from which is calculated distance
     * @param direction Direction that is checked
     * @return
     */
    public double rayCast(Vector pos, Vector direction) {
        double maxRange = 101;
        double leastDist = maxRange;
        double currentDist = 0;
        Vector ray;
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getLayer() != Layers.GROUND || gameObject.getHitBox() == null) {
                continue;
            }
            while (currentDist < 100) {
                ray = (Vector) direction.multiply(currentDist);
                if (gameObject.checkCollision(pos.add(ray))) {
                    leastDist = Math.min(currentDist, leastDist);
                    break;
                }
                currentDist += .01;
            }
            currentDist = 0;
        }
        return (leastDist == maxRange) ? 9000 : leastDist;
    }

}
