package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.GameAlreadyStartedException;
import it.polimi.ingsw.server.model.exception.NoSuchGameException;
import it.polimi.ingsw.server.model.exception.NoSuchReservedIdException;
import it.polimi.ingsw.server.model.exception.PlayersOutOfBoundException;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Singleton class that handles the creation, deletion and indexing of all the games currently instantiated.
 */

public class GameManager {
    private static GameManager instance = null;
    private final TreeMap<Integer, Game<? extends Turn>> gameMap;
    private final TreeMap<Integer, PairId<Integer, ArrayList<Player>>> reservedIds;

    private GameManager(){
        gameMap = new TreeMap<>();
        reservedIds = new TreeMap<>();
    }

    /**
     * @return the only instance of GameManager
     */
    public static synchronized GameManager getInstance(){
        if (instance == null) instance = new GameManager();
        return instance;
    }

    /**
     * @return an id not used in reservedId or gameMap
     */
    private synchronized int getNewId(){
        int i = 0;
        while (gameMap.containsKey(i) || reservedIds.containsKey(i)) {
            i++;
        }
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
     * @param id of the game to be created
     * @param player the player of the game
     */
    private synchronized void getNewSinglePlayer(int id, Player player){
        SinglePlayer singlePlayer = new SinglePlayer(player);
        gameMap.put(id, singlePlayer);
    }

    /**
     * Instantiates a new MultiPlayer and adds it to gameMap.
     *
     * @param id of the game to be created
     * @param players the ArrayList of players of the game
     */
    private synchronized void getNewMultiPlayer(int id, ArrayList<Player> players){
        MultiPlayer multiPlayer = new MultiPlayer(players);
        gameMap.put(id, multiPlayer);
    }

    /**
     * Removes a Game from gameMap.
     *
     * @param id of the game to destroy
     * @throws NoSuchGameException if there is no game with this id
     */
    public void removeGame(int id) throws NoSuchGameException {
        Game<? extends Turn> game = removeGameFromMap(id);
        if (game == null) throw new NoSuchGameException();
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

    /**
     * Reserves a new id for a game. If the game is single player it starts immediately, otherwise it waits for other players.
     *
     * @param playersNumber number of players for this game
     * @param userName name of the first player of the game
     * @return the id of the new game
     * @throws PlayersOutOfBoundException if playersNumber has a wrong value
     */
    public synchronized int reserveId (int playersNumber, String userName) {
        if (playersNumber < 1 || playersNumber > 4) throw new PlayersOutOfBoundException();
        int id = getNewId();
        Player player = new Player(userName, id*10);
        if(playersNumber == 1) getNewSinglePlayer(id, player);
        else {
            reservedIds.put(id, new PairId<>(playersNumber, new ArrayList<>() {{
                add(player);
            }}));
        }
        return id;
    }

    /**
     * Adds a player to a reservedId. If the list of players is full, the game begins.
     *
     * @param id of the game to add a player to
     * @param userName name of the new player of the game
     * @throws GameAlreadyStartedException if the id corresponds to a game already started
     * @throws NoSuchReservedIdException if the id is not in the reservedIds list
     */
    public synchronized void addPlayerToGame(int id, String userName) throws GameAlreadyStartedException, NoSuchReservedIdException {
        if(gameMap.containsKey(id)) throw new GameAlreadyStartedException();
        if(!reservedIds.containsKey(id)) throw new NoSuchReservedIdException();
        Player player = new Player(userName, id+reservedIds.get(id).getSecond().size());
        reservedIds.get(id).getSecond().add(player);
        if (reservedIds.get(id).getSecond().size() == reservedIds.get(id).getFirst()){
            ArrayList<Player> players = reservedIds.get(id).getSecond();
            Collections.shuffle(players);
            getNewMultiPlayer(id, players);
        }
    }
}
