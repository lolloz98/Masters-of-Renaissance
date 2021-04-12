package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.GameNotOverException;
import it.polimi.ingsw.model.exception.NoSuchGameException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Singleton class that handles the creation, deletion and indexing of all the games currently instantiated.
 */

public class GameManager {
    private static GameManager instance = null;
    private TreeMap<Integer, Game<? extends Turn>> gameMap;

    private GameManager(){
        gameMap = new TreeMap<>();
    }

    /**
     * @return the only instance of GameManager
     */
    public static synchronized GameManager getInstance(){
        if (instance == null) instance = new GameManager();
        return instance;
    }

    /**
     * Adds the game to gameMap
     *
     * @return the id of the new game
     */
    private synchronized int addGameToMap(Game<? extends Turn> game){
        int i = 0;
        while (gameMap.containsKey(i)) {
            i++;
        }
        gameMap.put(i, game);
        return i;
    }

    /**
     * @return the game corresponding to the id
     */
    public synchronized Game<? extends Turn> getGameFromMap(int id){
        return gameMap.get(id);
    }

    /**
     * Instantiates a new SinglePlayer and adds it to gameMap.
     *
     * @return the new id
     */
    public int getNewSinglePlayer(Player player){ // maybe it should return singlePlayer
        SinglePlayer singlePlayer = new SinglePlayer(player);
        return addGameToMap(singlePlayer);
    }

    /**
     * Instantiates a new MultiPlayer and adds it to gameMap.
     *
     * @return the new id
     */
    public int getNewMultiPlayer(ArrayList<Player> players){ // maybe it should return multiPlayer
        MultiPlayer multiPlayer = new MultiPlayer(players);
        return addGameToMap(multiPlayer);
    }

    /**
     * Destroys a Game and deletes it from gameMap.
     *
     * @param id of the game to destroy
     * @throws NoSuchGameException if there is no game with this id
     */
    public void destroyGame(int id) throws NoSuchGameException {
        Game<? extends Turn> game = removeGameFromMap(id);
        if (game == null) throw new NoSuchGameException();
        game.destroy();
    }

    /**
     * Removes a game from gameMap.
     *
     * @param id of the game to remove
     * @return the removed game, or null if it's not present
     */
    private synchronized Game<? extends Turn> removeGameFromMap(int id){
        return gameMap.remove(id);
    }
}
