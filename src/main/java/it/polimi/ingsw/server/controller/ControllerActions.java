package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.gameendedanswer.DestroyedGameAnswer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.controller.states.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Turn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class that handles the actions of the players and calls the methods of the model
 *
 * @param <T> the type of the game, it can be single player or multi player
 */
public abstract class ControllerActions<T extends Game<? extends Turn>> {
    private static final Logger logger = LogManager.getLogger(ControllerActions.class);

    protected T game;
    private final int gameId;
    private State gameState;
    private static final ControllerManager controllerManager = ControllerManager.getInstance();

    private final List<AnswerListener> listeners = Collections.synchronizedList(new ArrayList<>());

    /**
     * method called to create a controller
     *
     * @param game current game
     * @param id   gameId
     */
    public ControllerActions(T game, int id, AnswerListener answerListener) {
        this.game = game;
        this.gameId = id;
        addAnswerListener(answerListener);
    }

    public synchronized T getGame() {
        return game;
    }

    public synchronized int getGameId() {
        return gameId;
    }

    public abstract boolean checkToGamePlayState();

    public synchronized void toGamePlayState() {
        this.gameState = new GamePlayState();
    }

    public synchronized void toEndGameState() {
        this.gameState = new EndGameState();
    }

    public synchronized State getGameState() {
        return gameState;
    }

    public synchronized void doAction(ClientMessageController clientMessage) throws ControllerException {
        Answer answer = clientMessage.doAction(this);
        sendAnswer(answer);
    }

    /**
     * Do a pregame action: if the game is created send the entire game status to all players (each one will receive a slightly different status )
     *
     * @param clientMessage request
     * @param answerListener answerListener related to the player sending the request
     * @throws ControllerException if something not wanted happens
     */
    public synchronized void doPreGameAction(PreGameCreationMessageController clientMessage, AnswerListener answerListener) throws ControllerException {
        Answer answer = clientMessage.doAction(this);
        answerListener.setIds(answer.getPlayerId(), answer.getGameId());
        addAnswerListener(answerListener);
        if (game != null) {
            for (AnswerListener a : listeners) {
                a.sendAnswer(AnswerFactory.createGameStatusAnswer(answer.getGameId(), answer.getPlayerId(), a.getPlayerId(), game));
            }
        } else{
            sendAnswer(answer);
        }
    }

    private void sendAnswer(Answer answer) {
        // after each message sends answers to all the clients of this game
        listeners.forEach(x -> x.sendAnswer(answer));
    }

    protected void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public synchronized void addAnswerListener(AnswerListener answerListener) {
        listeners.removeIf(a ->
        {
            boolean cond = a.getPlayerId() == answerListener.getPlayerId();
            if (cond) logger.debug("Removed answerListener (it will be re-added) with playerId: " + a.getPlayerId());
            return cond;
        });
        logger.debug("adding answerListener " + answerListener.getPlayerId());
        listeners.add(answerListener);
    }

    public synchronized void removeAnswerListener(AnswerListener answerListener) {
        listeners.remove(answerListener);
    }

    public synchronized void doGetStatusAction(GameStatusMessageController parsedMessage) throws ControllerException {
        Answer gameStatusAnswer = parsedMessage.doAction(this);
        for(AnswerListener a: listeners){
            // we send the game status only to who required it
            if(a.getPlayerId() == gameStatusAnswer.getPlayerId()) a.sendAnswer(gameStatusAnswer);
        }
    }

    public abstract ArrayList<LocalTrack> getFaithTracks() throws ControllerException;

    public abstract void removeLeadersEffect() throws ControllerException;

    public abstract void applyLeadersEffect() throws ControllerException;

    /**
     * creates the end game answer by putting in it the list of the winners
     * @throws ControllerException if something goes wrong
     */
    public abstract ArrayList<LocalPlayer> getWinners() throws ControllerException;

    /**
     * it "destroys" the game. after this the game nor the controllers are accessible anymore and there is no reference to them
     * @param message message to be sent in the destruction answer
     * @param playerId player who called destroyGame
     * @param removePlayerIdListener if true remove the answerListener related to playerId before sending back the answer
     */
    public synchronized void destroyGame(String message, int playerId, boolean removePlayerIdListener) throws NoSuchControllerException {
        Answer destroyAnswer = new DestroyedGameAnswer(getGameId(), playerId, message);
        controllerManager.removeGame(getGameId());

        listeners.removeIf(x -> removePlayerIdListener && x.getPlayerId() == playerId);

        for(AnswerListener answerListener: listeners){
            // before sending the answer remove the answer listener
            answerListener.setIds(-1, -1);
        }

        sendAnswer(destroyAnswer);

        listeners.clear();
    }

}
