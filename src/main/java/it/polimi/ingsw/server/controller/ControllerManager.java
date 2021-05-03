package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.model.exception.GameAlreadyStartedException;
import it.polimi.ingsw.server.model.exception.NoSuchGameException;
import it.polimi.ingsw.server.model.exception.NoSuchReservedIdException;
import it.polimi.ingsw.server.model.exception.PlayersOutOfBoundException;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;
import it.polimi.ingsw.server.requests.CreateGameMessage;
import it.polimi.ingsw.server.requests.JoinGameMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Singleton class that handles the creations of the model with his corresponding controller
 */
public class ControllerManager {
    private static ControllerManager instance=null;
    /**
     * treemap containing the id of the game and the ControllerActions associated
     */
    private final TreeMap<Integer,ControllerActions> controllerMap;
    private final TreeMap<Integer, Game<? extends Turn>> gameMap;
    private final TreeMap<Integer, PairId<Integer, ArrayList<Player>>> reservedIds;

   private ControllerManager(){
       controllerMap=new TreeMap<>();
       gameMap = new TreeMap<>();
       reservedIds = new TreeMap<>();
   }

    /**
     * @return the only instance of ControllerHandler
     */
    public static synchronized ControllerManager getInstance(){
        if (instance == null) instance = new ControllerManager();
        return instance;
    }

    /**
     * method that handle the request of creation a new game
     * @param message message by the first player
     * @return the id of the game
     */
    public synchronized int createGame(CreateGameMessage message) throws ControllerException {
        int id;
        try {
            id = reserveId(message.getPlayersNumber(), message.getUserName());
        } catch (PlayersOutOfBoundException e) {
            //todo
            throw new ControllerException();
        }
        return id;
    }

    public synchronized void joinGame(JoinGameMessage message) throws ControllerException {
        try {
            addPlayerToGame(message.getPlayerId(), message.getUserName());
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
            throw new ControllerException();
        } catch (NoSuchReservedIdException e) {
            e.printStackTrace();
            throw new ControllerException();
        }
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
    private synchronized Game<? extends Turn> getGameFromMap(int id){
        return gameMap.get(id);
    }

    /**
     * @return the controller corresponding to the id
     */
    public synchronized ControllerActions getControllerFromMap(int id){
        return controllerMap.get(id);
    }

    /**
     * Instantiates a new SinglePlayer and adds it to gameMap.
     *
     * @param id of the game to be created
     * @param player the player of the game
     */
    private synchronized void getNewSinglePlayer(int id, Player player){
        SinglePlayer singlePlayer = new SinglePlayer(player);
        controllerMap.put(id,new ControllerActionsSingle(singlePlayer,id));
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
        controllerMap.put(id,new ControllerActionsMulti(multiPlayer,id));
        gameMap.put(id, multiPlayer);
        reservedIds.remove(id);
    }

    /**
     * Removes a Game from gameMap.
     *
     * @param id of the game to destroy
     * @throws NoSuchGameException if there is no game with this id
     */
    public void removeGame(int id) throws NoSuchGameException, NoSuchControllerException {
        if (!gameMap.containsKey(id)) throw new NoSuchGameException();
        if (!controllerMap.containsKey(id)) throw new NoSuchControllerException();
        removeGameFromMap(id);
        controllerMap.remove(id);
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
    private synchronized int reserveId (int playersNumber, String userName) {
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
    private synchronized void addPlayerToGame(int id, String userName) throws GameAlreadyStartedException, NoSuchReservedIdException {
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
