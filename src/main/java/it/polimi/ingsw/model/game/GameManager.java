package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.TreeMap;

public class GameManager {
    private static GameManager instance = null;
    private TreeMap<Integer, Game<? extends Turn>> gameMap;

    private GameManager(){
        gameMap = new TreeMap<>();
    }

    public static synchronized GameManager getInstance(){
        if (instance == null) instance = new GameManager();
        return instance;
    }

    private synchronized int addGameToMap(Game<? extends Turn> game){
        int i = 0;
        while (gameMap.containsKey(i)) {
            i++;
        }
        gameMap.put(i, game);
        return i;
    }

    private synchronized Game<? extends Turn> getGameFromMap(int id){
        return gameMap.get(id);
    }

    private int getNewSinglePlayer(Player player){ // maybe it should return singlePlayer
        SinglePlayer singlePlayer = new SinglePlayer(player);
        return addGameToMap(singlePlayer);
    }

    private int getNewMultiPlayer(ArrayList<Player> players){ // maybe it should return multiPlayer
        MultiPlayer multiPlayer = new MultiPlayer(players);
        return addGameToMap(multiPlayer);
    }

    private void destroyGame(int id){
        removeGameFromMap(id).destroy();
    }

    public synchronized Game<? extends Turn> removeGameFromMap(int id){
        return gameMap.remove(id);
    }
}
