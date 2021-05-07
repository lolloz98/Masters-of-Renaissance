package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Singleton class that handles the creation of games with their corresponding controllers
 */
public class ControllerManager {

    private static final Logger logger = LogManager.getLogger(ControllerManager.class);

    private static ControllerManager instance = null;
    /**
     * treemap containing the id of the game and the ControllerActions associated
     */
    private final TreeMap<Integer, ControllerActions<?>> controllerMap;
    private final TreeMap<Integer, PairId<Integer, ArrayList<Player>>> reservedIds;

    private ControllerManager() {
        controllerMap = new TreeMap<>();
        reservedIds = new TreeMap<>();
    }

    /**
     * @return the only instance of ControllerHandler
     */
    public static synchronized ControllerManager getInstance() {
        if (instance == null) instance = new ControllerManager();
        return instance;
    }

    /**
     * method that handles the request of creation a new game
     *
     * @param message message by the first player
     * @return a pair with the id of the game and of the player
     */
    public synchronized PairId<Integer, Integer> reserveIdForNewGame(CreateGameMessage message) throws ControllerException {
        return reserveId(message.getPlayersNumber(), message.getUserName());
    }

    /**
     * @param message request of a client to join the game
     * @return id of the added player
     * @throws ControllerException if the information about the game in message are not valid
     */
    public synchronized int joinGame(JoinGameMessage message) throws ControllerException {
        return addPlayerToGame(message.getGameId(), message.getUserName());
    }

    /**
     * @return an id not used in reservedId or gameMap
     */
    private synchronized int getNewId() {
        int i = 0;
        while (controllerMap.containsKey(i) || reservedIds.containsKey(i)) {
            // fixme: check i for upper bounds
            i++;
        }
        return i;
    }

    /**
     * @return the game corresponding to the id
     */
    private synchronized Game<? extends Turn> getGameFromMap(int id) {
        return controllerMap.get(id).getGame();
    }

    /**
     * @return the controller corresponding to the id
     */
    public synchronized ControllerActions<?> getControllerFromMap(int id) {
        return controllerMap.get(id);
    }

    /**
     * Instantiates a new SinglePlayer and adds it to gameMap.
     *
     * @param id     of the game to be created
     * @param player the player of the game
     */
    private synchronized void createNewSinglePlayer(int id, Player player) {
        SinglePlayer singlePlayer = new SinglePlayer(player);
        controllerMap.put(id, new ControllerActionsSingle(singlePlayer, id));
    }

    /**
     * Instantiates a new MultiPlayer and adds it to gameMap.
     *
     * @param id      of the game to be created
     * @param players the ArrayList of players of the game
     */
    private synchronized void createNewMultiPlayer(int id, ArrayList<Player> players) {
        MultiPlayer multiPlayer = new MultiPlayer(players);
        controllerMap.put(id, new ControllerActionsMulti(multiPlayer, id));
        reservedIds.remove(id);
    }

    /**
     * Removes a Game from gameMap.
     *
     * @param id of the game to destroy
     * @throws NoSuchControllerException if there is no game with this id
     */
    public synchronized void removeGame(int id) throws NoSuchControllerException {
        if (!controllerMap.containsKey(id)) throw new NoSuchControllerException();
        controllerMap.remove(id);
    }

    /**
     * Reserves a new id for a game. If the game is single player it starts immediately, otherwise it waits for other players.
     *
     * @param playersNumber number of players for this game
     * @param userName      name of the first player of the game
     * @return a pair with the id of the new game and the id of the player
     * @throws PlayersOutOfBoundControllerException if playersNumber has a wrong value
     */
    private synchronized PairId<Integer, Integer> reserveId(int playersNumber, String userName) throws PlayersOutOfBoundControllerException {
        if (playersNumber < 1 || playersNumber > 4) throw new PlayersOutOfBoundControllerException();
        int id = getNewId();
        int playerId = id * 10;
        Player player = new Player(userName, playerId);
        if (playersNumber == 1) createNewSinglePlayer(id, player);
        else {
            reservedIds.put(id, new PairId<>(playersNumber, new ArrayList<>() {{
                add(player);
            }}));
        }
        return new PairId<>(id, playerId);
    }

    /**
     * Adds a player to a reservedId. If the list of players is full, the game begins.
     *
     * @param id       of the game to add a player to
     * @param userName name of the new player of the game
     * @return the playerId of the added player
     * @throws GameAlreadyStartedControllerException if the id corresponds to a game already started
     * @throws NoSuchReservedIdControllerException   if the id is not in the reservedIds list
     */
    private synchronized int addPlayerToGame(int id, String userName) throws GameAlreadyStartedControllerException, NoSuchReservedIdControllerException {
        if(controllerMap.containsKey(id)) throw new GameAlreadyStartedControllerException();
        if (!reservedIds.containsKey(id)) throw new NoSuchReservedIdControllerException();

        if(reservedIds.get(id).getFirst() >= reservedIds.get(id).getSecond().size())
            logger.error("player size exceeds the number specified by the creator of the game");
        int playerId = id * 10 + reservedIds.get(id).getSecond().size();
        Player player = new Player(userName, playerId);
        reservedIds.get(id).getSecond().add(player);
        if (reservedIds.get(id).getSecond().size() == reservedIds.get(id).getFirst()) {
            ArrayList<Player> players = reservedIds.get(id).getSecond();
            Collections.shuffle(players);
            createNewMultiPlayer(id, players);
        }
        return playerId;
    }
}
