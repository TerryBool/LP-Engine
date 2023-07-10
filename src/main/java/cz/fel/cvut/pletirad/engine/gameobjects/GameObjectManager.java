package cz.fel.cvut.pletirad.engine.gameobjects;

import cz.fel.cvut.pletirad.game.player.Player;

import java.util.ArrayList;

public class GameObjectManager {
    ArrayList<GameObject> gameObjectList;

    private Player player;

    public GameObjectManager() {
        gameObjectList = new ArrayList<>();

    }

    public void addObject(GameObject go) {
        gameObjectList.add(go);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void destroyObject(GameObject go) {
        gameObjectList.remove(go);
    }

    public ArrayList<GameObject> getObjectList() {
        return gameObjectList;
    }
}
