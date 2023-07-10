package cz.fel.cvut.pletirad.engine.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.game.enemies.skeleton.Skeleton;
import cz.fel.cvut.pletirad.game.player.Player;
import cz.fel.cvut.pletirad.game.testobjects.Door;
import cz.fel.cvut.pletirad.game.testobjects.KeyObject;
import cz.fel.cvut.pletirad.game.testobjects.Platform;
import cz.fel.cvut.pletirad.game.testobjects.Wall;

import java.io.File;

public class LevelManager {

    private final String TESTING_LEVEL_NAME = "src/main/resources/Levels/TestingLevel.json";

    private GameObjectManager gom;
    private final InputHandler ih;
    private CollisionDetector cd;

    public LevelManager(GameObjectManager gom, InputHandler ih, CollisionDetector cd) {
        this.gom = gom;
        this.cd = cd;
        this.ih = ih;
    }

    public void generateLevel() {
        Player player = new Player();
        player.initPlayer(ih, cd);
        gom.addObject(player);
        gom.setPlayer(player);
        gom.addObject(new Door(600, 313));
        // gom.addObject(new Wraith(50, 100, 20, 260, cd));
        //gom.addObject(new Slime(20, 100, 20, 260, cd));
        gom.addObject(new Skeleton(30, 100, 30, 260, cd));

        int x = 0;
        while (x < 2720) {
            gom.addObject(new Platform(x, 360));
            x += 48;
        }
        gom.addObject(new KeyObject(280, 320));
        gom.addObject(new Wall(20, 314));
        gom.addObject(new Wall(680, 314));
    }

    public void saveTestingLevel() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            objectMapper.writeValue(new File(TESTING_LEVEL_NAME), gom);
        } catch (Exception e) {
            System.err.println("Error in saving testing level!");
            e.printStackTrace();
        }
    }

    public GameObjectManager loadTestingLevel() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            GameObjectManager gom = objectMapper.readValue(new File(TESTING_LEVEL_NAME), GameObjectManager.class);
            cd.updateObjectList(gom.getObjectList());
            this.gom = gom;
            for (GameObject go : this.gom.getObjectList()) {
                if (go.getLayer() == Layers.ENEMY) {
                    Enemy enemy = (Enemy) go;
                    enemy.updateCollisionDetector(cd);
                }
                if (go.getLayer() == Layers.PLAYER) {
                    Player player = (Player) go;
                    player.initPlayer(ih, cd);
                    this.gom.setPlayer(player);
                }
            }
            return gom;
        } catch (Exception e) {
            System.err.println("Failed to load testing level!");
            e.printStackTrace();
            return null;
        }
    }
}
