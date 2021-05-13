package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.exception.*;
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

    private ControllerManager() {
        controllerMap = new TreeMap<>();
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
    public synchronized PairId<Integer, Integer> reserveIdForNewGame(CreateGameMessage message, AnswerListener answerListener) throws ControllerException {
        PairId<Integer, Integer> p = reserveId(message.getPlayersNumber(), message.getUserName(), answerListener);
        answerListener.setPlayerId(p.getSecond());
        return p;
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
        while (controllerMap.containsKey(i)) {
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
    public synchronized ControllerActions<?> getControllerFromMap(int id) throws ControllerException {
        ControllerActions<?> tmp = controllerMap.get(id);
        if(tmp == null) throw new ControllerException("No game with specified id");
        return tmp;
    }

    /**
     * Instantiates a new SinglePlayer and adds it to gameMap.
     *
     * @param id     of the game to be created
     * @param player the player of the game
     */
    private synchronized void createNewSinglePlayer(int id, AnswerListener answerListener, Player player) throws UnexpectedControllerException {
        SinglePlayer singlePlayer = null;
        try {
            singlePlayer = new SinglePlayer(player);
        } catch (ModelException e) {
            logger.error("something went wrong with the creation of a singlePlayer");
            throw new UnexpectedControllerException("something went wrong with the creation of a singlePlayer");
        }
        controllerMap.put(id, new ControllerActionsSingle(singlePlayer, id, answerListener));
    }

    /**
     * Instantiates a new ControllerActionsMulti and adds it to gameMap.
     *
     * @param id      of the game to be created
     * @param numberOfPlayers the number of players that this game must have
     * @param player the player who asked for the creation of the game
     */
    private synchronized void createNewControllerActionsMulti(int id, AnswerListener answerListener, int numberOfPlayers, Player player) {
        controllerMap.put(id, new ControllerActionsMulti(id, answerListener, numberOfPlayers, player));
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
    private synchronized PairId<Integer, Integer> reserveId(int playersNumber, String userName, AnswerListener answerListener) throws PlayersOutOfBoundControllerException, UnexpectedControllerException {
        if (playersNumber < 1 || playersNumber > 4) throw new PlayersOutOfBoundControllerException();
        int id = getNewId();
        int playerId = id * 10;
        Player player = null;
        try {
            player = new Player(userName, playerId);
        } catch (InvalidArgumentException e) {
            throw new UnexpectedControllerException(e.getMessage());
        }
        if (playersNumber == 1) createNewSinglePlayer(id, answerListener, player);
        else {
            createNewControllerActionsMulti(id, answerListener, playersNumber, player);
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
    private synchronized int addPlayerToGame(int id, String userName) throws GameAlreadyStartedControllerException, NoSuchReservedIdControllerException, UnexpectedControllerException {
        if(!controllerMap.containsKey(id)) throw new NoSuchReservedIdControllerException();
        if (controllerMap.get(id).getGame() != null) throw new GameAlreadyStartedControllerException();

        // if I am here the game is surely multiplayer (otherwise the game cannot be null)
        ControllerActionsMulti controllerActionsMulti = (ControllerActionsMulti) controllerMap.get(id);
        // I get the reference to numberAndPlayers
        PairId<Integer, ArrayList<Player>> numberAndPlayers = controllerActionsMulti.getNumberAndPlayers();

        if(numberAndPlayers.getFirst() <= numberAndPlayers.getSecond().size()) {
            logger.error("player size exceeds the number specified by the creator of the game");
            throw new UnexpectedControllerException("the game has reached the specified number of players already");
        }

        int playerId = id * 10 + numberAndPlayers.getSecond().size();
        Player player = null;
        try {
            player = new Player(userName, playerId);
        } catch (InvalidArgumentException e) {
            throw new UnexpectedControllerException(e.getMessage());
        }

        controllerActionsMulti.getNumberAndPlayers().getSecond().add(player);
        if (numberAndPlayers.getSecond().size() == numberAndPlayers.getFirst()) {
            ArrayList<Player> players = numberAndPlayers.getSecond();
            Collections.shuffle(players);
            try {
                controllerActionsMulti.setGame(new MultiPlayer(players));
            } catch (ModelException e) {
                logger.error("something went wrong with the creation of a multiPlayer");
                throw new UnexpectedControllerException("something went wrong with the creation of a multiPlayer");
            }
        }
        return playerId;
    }
}
