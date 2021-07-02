package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.PersistenceDirectoryLocator;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.Persist;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.exception.NoSuchGameException;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton class that handles the creation of games and their corresponding controllers.
 * It also handles the dispatch of games and their destruction
 */
public class ControllerManager {

    private static final Logger logger = LogManager.getLogger(ControllerManager.class);

    private static ControllerManager instance = null;
    /**
     * treemap containing the id of the game and the ControllerActions associated
     */
    private final TreeMap<Integer, ControllerActionsServer<?>> controllerMap;
    /**
     * treeSet containing the id of the game to which someone could rejoin
     */
    private final TreeSet<Integer> toBeRejoinedIds;
    /**
     * games in which all players have not yet joined and the game is still not
     * created
     */
    private final TreeSet<Integer> joinGamesIds = new TreeSet<>();

    private ControllerManager() {
        controllerMap = new TreeMap<>();
        toBeRejoinedIds = new TreeSet<>();
        // load from files
        final File folder = new File(PersistenceDirectoryLocator.getDir());
        if(!folder.exists()) {
            if (folder.mkdirs()){
                logger.debug("folder created at: " + PersistenceDirectoryLocator.getDir());
            }else{
                logger.error("folder not created");
            }
        }

        List<File> files = List.of(Objects.requireNonNull(folder.listFiles()));
        List<String> fileNames = files.stream().filter(File::isFile).map(File::getName).collect(Collectors.toList());
        for (String fileName : fileNames) {
            if (fileName.endsWith(".tmp")) {
                logger.debug("adding game from file: " + fileName);
                try {
                    String idS = fileName.substring(4);
                    int id = Integer.parseInt(idS.split(".tmp")[0]);
                    Game<?> game = Persist.getInstance().retrieve(id);
                    toBeRejoinedIds.add(id);
                    if (game instanceof MultiPlayer) {
                        controllerMap.put(id, new ControllerActionsServerMulti((MultiPlayer) game, id));
                    } else {
                        ControllerActionsServerSingle controllerActionsServerSingle = new ControllerActionsServerSingle((SinglePlayer) game, id);
                        controllerMap.put(id, controllerActionsServerSingle);
                    }
                } catch (NoSuchGameException | UnexpectedControllerException e) {
                    logger.error("error while reading file: " + fileName);
                } catch (IllegalArgumentException e) {
                    logger.error("error while parsing name of file: " + e);
                }
            }
        }
        Timer timer = new Timer();
        logger.debug("setting up timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeToBeRejoined();
            }
        }, 2*60*1000);
    }

    private synchronized void removeToBeRejoined() {
        logger.debug("removing to be rejoined");
        for(int e: toBeRejoinedIds){
            controllerMap.remove(e);
            Persist.getInstance().remove(e);
        }
        toBeRejoinedIds.clear();
    }

    /**
     * @return the only instance of ControllerHandler
     */
    public static synchronized ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
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
    private synchronized int getNewId() throws UnexpectedControllerException {
        int i = 0;
        while (controllerMap.containsKey(i)) {
            i++;
        }
        if(i > Integer.MAX_VALUE / 10){
            throw new UnexpectedControllerException("Too many games. Wait until one of them finishes");
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
    public synchronized ControllerActionsServer<?> getControllerFromMap(int id) throws NoSuchControllerException {
        ControllerActionsServer<?> tmp = controllerMap.get(id);
        if(tmp == null) throw new NoSuchControllerException();
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
        try {
            controllerMap.put(id, new ControllerActionsServerSingle(singlePlayer, id, answerListener));
        } catch (EmptyDeckException e) {
            logger.error("something went wrong with the creation of a multiPlayer");
            throw new UnexpectedControllerException("something went wrong with the creation of a singlePlayer");
        }
    }

    /**
     * Instantiates a new ControllerActionsMulti and adds it to gameMap.
     *
     * @param id      of the game to be created
     * @param numberOfPlayers the number of players that this game must have
     * @param player the player who asked for the creation of the game
     */
    private synchronized void createNewControllerActionsMulti(int id, AnswerListener answerListener, int numberOfPlayers, Player player) {
        controllerMap.put(id, new ControllerActionsServerMulti(id, answerListener, numberOfPlayers, player));
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
        answerListener.setIds(playerId, id);
        if (playersNumber == 1) createNewSinglePlayer(id, answerListener, player);
        else {
            createNewControllerActionsMulti(id, answerListener, playersNumber, player);
            joinGamesIds.add(id);
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
        if(!joinGamesIds.contains(id)) throw new NoSuchReservedIdControllerException();

        // if I am here the game is surely multiplayer (otherwise the game cannot be null)
        ControllerActionsServerMulti controllerActionsMulti = (ControllerActionsServerMulti) controllerMap.get(id);
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
                joinGamesIds.remove(id);
            } catch (ModelException e) {
                logger.error("something went wrong with the creation of a multiPlayer");
                throw new UnexpectedControllerException("something went wrong with the creation of a multiPlayer");
            }
        }
        return playerId;
    }

    public synchronized boolean removeFromToBeRejoined(int gameId){
        return toBeRejoinedIds.remove(gameId);
    }
}
