package cz.fel.cvut.pletirad.engine.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObject;
import cz.fel.cvut.pletirad.engine.gameobjects.GameObjectManager;
import cz.fel.cvut.pletirad.engine.gameobjects.Layers;
import cz.fel.cvut.pletirad.engine.gameobjects.gotypes.Enemy;
import cz.fel.cvut.pletirad.engine.inputs.InputHandler;
import cz.fel.cvut.pletirad.game.enemies.skeleton.Skeleton;
import cz.fel.cvut.pletirad.game.enemies.slime.Slime;
import cz.fel.cvut.pletirad.game.enemies.wraith.Wraith;
import cz.fel.cvut.pletirad.game.items.bomb.BombObject;
import cz.fel.cvut.pletirad.game.items.key.KeyObject;
import cz.fel.cvut.pletirad.game.items.orb.OrbObject;
import cz.fel.cvut.pletirad.game.player.Player;
import cz.fel.cvut.pletirad.game.testobjects.*;

import java.io.File;

/**
 * Class used for saving, loading and generating levels.
 */

public class LevelManager {

    private final String TESTING_LEVEL_NAME = "TestingLevel.json";
    private final String latestSave = "UserSave.json";

    private GameObjectManager gom;
    private final InputHandler ih;
    private CollisionDetector cd;

    private boolean hasSave = false;

    public LevelManager(GameObjectManager gom, InputHandler ih, CollisionDetector cd) {
        this.gom = gom;
        this.cd = cd;
        this.ih = ih;
    }

    /**
     * Initial level generation, after it's saved to json it can be loaded and played normally.
     */
    public void generateLevel() {
        Player player = new Player();
        player.initPlayer(ih, cd);
        gom.addObject(player);
        gom.setPlayer(player);


        int x = 320;
        int y = 256;
        while (x < 850) {
            gom.addObject(new Platform(x, y));
            x += 48;
        }

        //Starting position, basic house first steps
        gom.addObject(new BrickWall(320, 130));
        gom.addObject(new BrickWall(320, 164));
        gom.addObject(new BrickWall(320, 210));
        gom.addObject(new BrickWall(464, 114));
        for (x = 320; x < 420; x += 48) {
            gom.addObject(new BrickPlatform(x, 114));
            gom.addObject(new BrickPlatform(x + 16, 240));
        }
        //first enemy encounter
        gom.addObject(new Slime(580, 620, 600, y - 80, cd));
        gom.addObject(new BombObject(600, y - 50));

        //jumping platforms
        for (x = 870; x < 1200; x += 96) {
            y -= 85;
            gom.addObject(new Platform(x, y));

        }

        //tougher enemy ahead
        for (x = x - 48; x < 2000; x += 48) {
            gom.addObject(new Platform(x, y));
        }
        gom.addObject(new Wraith(1500, 1500, 1500, y-100, cd));

        for (x = 1578; x < 2000; x+= 48) {
            gom.addObject(new Platform(x, y - 96));
        }
        gom.addObject(new Door(x-48, y-47));
        gom.addObject(new DirtWall(x-48, y-95));
        gom.addObject(new Skeleton(x-48, x-48, x-48, y - 190, cd));
        for (x = x - 48; x < 2200; x += 48) {
            gom.addObject(new BrickPlatform(x, y));
            gom.addObject(new BrickPlatform(x, y-96));
        }
        gom.addObject(new OrbObject(2100, y - 60));
        gom.addObject(new KeyObject(1800, y - 60));
        gom.addObject(new BrickWall(x - 16, y - 46));
        gom.addObject(new BrickWall(x - 16, y - 92));

    }

    /**
     * Saves current sate of the game. Always rewrites previous save
     */
    public void saveCurrentState() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            objectMapper.writeValue(new File(latestSave), gom);
            hasSave = true;
        } catch (Exception e) {
            System.err.println("Error in saving testing level!");
            e.printStackTrace();
        }
    }

    /**
     * Loads latest game save
     *
     * @return Game object manager that holds the state of the game. Has to get into game container to work
     */
    public GameObjectManager loadLatestSave() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            GameObjectManager gom = objectMapper.readValue(new File(latestSave), GameObjectManager.class);
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
            System.err.println("Failed to load latest save!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saving of initially generated level. Used mainly for testing purposes
     */
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

    /**
     * Load of initially generated level. Used primarily for testing purposes
     *
     * @return Game object manager that holds the state of the game. Has to get into game container to work
     */
    public GameObjectManager loadTestingLevel() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
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

    public boolean hasSave() {
        return hasSave;
    }
}
