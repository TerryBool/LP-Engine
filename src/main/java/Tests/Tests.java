package Tests;

import cz.fel.cvut.pletirad.engine.HitBox;
import cz.fel.cvut.pletirad.engine.Vector;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.logic.CollisionDetector;
import cz.fel.cvut.pletirad.game.items.key.Key;
import cz.fel.cvut.pletirad.game.player.Player;
import cz.fel.cvut.pletirad.game.testobjects.Door;
import cz.fel.cvut.pletirad.game.testobjects.Platform;
import cz.fel.cvut.pletirad.game.testobjects.Wall;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Tests {

    @Test
    public void collisionDetectorDetectCollisionTest() {
        GameObject objectToKill = new GameObject() {
            private boolean move = false;
            @Override
            public void update() {
                if(pos == null) {
                    pos = new Vector(0,0);
                }
                if(move){
                    pos = pos.add(new Vector(2,0));
                }
                hitBox = new HitBox(pos.getX(), pos.getY(), 1,1);
                getDestroy();
                move = true;
            }

            @Override
            public void render(GraphicsContext gc, Vector cameraOffset) {

            }

            @Override
            public void onCollision(GameObject go) {
                killObject();
            }
        };

        GameObject assassin = new GameObject() {
            @Override
            public void update() {
                if(pos == null) {
                    pos = new Vector(2,0);
                }
                hitBox = new HitBox(pos.getX(), pos.getY(), 1,1);
            }

            @Override
            public void render(GraphicsContext gc, Vector cameraOffset) {

            }

            @Override
            public void onCollision(GameObject go) {

            }
        };

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(assassin);
        gameObjects.add(objectToKill);

        CollisionDetector cd = new CollisionDetector(gameObjects);

        assassin.update();
        objectToKill.update();

        cd.checkCollisions(objectToKill);

        if(objectToKill.getDestroy()){
            fail("Object was destroyed before collision was detected!");
        }

        objectToKill.update();
        cd.checkCollisions(objectToKill);

        assertTrue("Object was not destroyed!", objectToKill.getDestroy());
    }

    @Test
    public void collisionDetectorProjectionHorizontalTest() {

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(new Platform(0,0));
        gameObjects.add(new Wall(0,-46));

        CollisionDetector cd = new CollisionDetector(gameObjects);

        HitBox cloneLeft = new HitBox(20,-3,1,1);
        Vector moveLeft = new Vector(-3,0);

        moveLeft = cd.projectionCast(cloneLeft, moveLeft);

        HitBox cloneRight = new HitBox(-2,-3,1,1);
        Vector moveRight = new Vector(0,0);

        moveRight = cd.projectionCast(cloneRight, moveRight);

        cloneLeft = new HitBox(moveLeft.getX() + cloneLeft.getMinX(),
                moveLeft.getY() + cloneLeft.getMinY(), 1,1);
        cloneRight = new HitBox(moveRight.getX() + cloneRight.getMinX(),
                moveRight.getY() + cloneRight.getMinY(), 1,1);

        for (GameObject go:gameObjects) {
            if(go.checkCollision(cloneLeft)) {
                fail("Fail, collision on left");
            }
            if(go.checkCollision(cloneRight)){
                fail("Fail, collision on right");
            }
        }
        assertTrue("Found a collision, poopies", true);
    }

    @Test
    public void collisionDetectorProjectionVerticalTest() {

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(new Platform(0,0));
        gameObjects.add(new Platform(0,-20));

        CollisionDetector cd = new CollisionDetector(gameObjects);

        HitBox cloneVertical = new HitBox(-5,-2,1,1);
        Vector moveUp = new Vector(0,-4);

        moveUp = cd.projectionCast(cloneVertical, moveUp);

        Vector moveDown = new Vector(0,4);

        moveDown = cd.projectionCast(cloneVertical, moveDown);

        HitBox cloneDown = new HitBox(moveDown.getX() + cloneVertical.getMinX(),
                moveDown.getY() + cloneVertical.getMinY(), 1,1);
        HitBox cloneUp = new HitBox(moveUp.getX() + cloneVertical.getMinX(),
                moveUp.getY() + cloneVertical.getMinY(), 1,1);

        for (GameObject go:gameObjects) {
            if(go.checkCollision(cloneDown)) {
                fail("Fail, collision above");
            }
            if(go.checkCollision(cloneUp)){
                fail("Fail, collision below");
            }
        }
        assertTrue("Found a collision, poopies", true);
    }

    @Test
    public void collisionDetectorProjectionDiagonalTest() {

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(new Platform(-4,0));
        gameObjects.add(new Platform(-4,-20));
        gameObjects.add(new Wall(-16,-16));
        gameObjects.add(new Wall(4,-16));

        CollisionDetector cd = new CollisionDetector(gameObjects);

        HitBox cloneDiagonal = new HitBox(2,-2,1,1);

        Vector moveDR = new Vector(3,3);
        moveDR = cd.projectionCast(cloneDiagonal, moveDR);

        Vector moveUR = new Vector(3,-3);
        moveUR = cd.projectionCast(cloneDiagonal, moveUR);

        Vector moveUL = new Vector(-3,-3);
        moveUL =  cd.projectionCast(cloneDiagonal, moveUL);

        Vector moveDL = new Vector(-3,3);
        moveDL = cd.projectionCast(cloneDiagonal, moveDL);

        HitBox cloneDR = new HitBox(moveDR.getX() + cloneDiagonal.getMinX(),
                moveDR.getY() + cloneDiagonal.getMinY(), 1,1);

        HitBox cloneUR = new HitBox(moveUR.getX() + cloneDiagonal.getMinX(),
                moveUR.getY() + cloneDiagonal.getMinY(), 1,1);

        HitBox cloneUL = new HitBox(moveUL.getX() + cloneDiagonal.getMinX(),
                moveUL.getY() + cloneDiagonal.getMinY(), 1,1);

        HitBox cloneDL = new HitBox(moveDL.getX() + cloneDiagonal.getMinX(),
                moveDL.getY() + cloneDiagonal.getMinY(), 1,1);



        for (GameObject go:gameObjects) {
            if(go.checkCollision(cloneDR)) {
                fail("Fail, collision down right");
            }
            if(go.checkCollision(cloneUR)) {
                fail("Fail, collision up right");
            }
            if(go.checkCollision(cloneUL)) {
                fail("Fail, collision up left");
            }
            if(go.checkCollision(cloneDL)) {
                fail("Fail, collision down left");
            }
        }
        assertTrue("", true);
    }

    @Test
    public void doorInteractTest() {
        Player player = new Player(true);
        Door doors = new Door();
        doors.interact(player);
        if(doors.getDestroy()) {
            fail("Fail, player does not have key");
        }
        player.getItemList().add(new Key());
        doors.interact(player);
        assertTrue("Doors were not destroyed after being opened", doors.getDestroy());
    }
}
