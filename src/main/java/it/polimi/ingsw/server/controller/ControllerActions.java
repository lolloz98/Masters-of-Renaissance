package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.GameStatusMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.controller.states.EndGameState;
import it.polimi.ingsw.server.controller.states.GamePlayState;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.controller.states.State;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * class that handles the actions of the players and calls the methods of the model
 *
 * @param <T> the type of the game, it can be single player or multi player
 */
public abstract class ControllerActions<T extends Game<?>> {
    private static final Logger logger = LogManager.getLogger(ControllerActions.class);

    protected T game;
    private final int gameId;
    private State gameState;
    private static final ControllerManager controllerManager = ControllerManager.getInstance();

    private final ArrayList<AnswerListener> listeners = new ArrayList<>();

    /**
     * method called to create a controller
     *
     * @param game
     * @param id   gameId
     */
    public ControllerActions(T game, int id, AnswerListener answerListener) {
        this.game = game;
        this.gameId = id;
        this.listeners.add(answerListener);
        gameState = new PrepareGameState();
    }

    public synchronized T getGame() {
        return game;
    }

    public synchronized int getGameId() {
        return gameId;
    }

    /**
     * method that changes the state of the game: from waitingState to prepareGameState
     * and prepares the game to be played
     */
    public synchronized void toPrepareGameState() throws UnexpectedControllerException {
        try {
            game.distributeLeader();
        } catch (EmptyDeckException e) {
            throw new UnexpectedControllerException("The deck of leader is empty before having distributed the cards to the players");
        }
        this.gameState = new PrepareGameState();
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

    public synchronized void doPreGameAction(PreGameCreationMessageController clientMessage, AnswerListener answerListener) throws ControllerException {
        Answer answer = clientMessage.doAction(this);
        answerListener.setPlayerId(answer.getPlayerId());
        addAnswerListener(answerListener);
        if (game != null) {
            for (AnswerListener a : listeners) {
                answerListener.sendAnswer(AnswerFactory.createGameStatusAnswer(answer.getGameId(), answer.getPlayerId(), a.getPlayerId(), game));
            }
        } else{
            sendAnswer(answer);
        }
    }

    private void sendAnswer(Answer answer) {
        // after each message sends answers to all the clients of this game
        listeners.forEach(x -> x.sendAnswer(answer));
    }

    public synchronized void addAnswerListener(AnswerListener answerListener) {
        listeners.removeIf(a ->
        {
            boolean cond = a.getPlayerId() == answerListener.getPlayerId();
            if (cond) logger.debug("Removed answerListener (it will be re-added) with playerId: " + a.getPlayerId());
            return cond;
        });
        listeners.add(answerListener);
    }

    public synchronized void removeAnswerListener(AnswerListener answerListener) {
        listeners.remove(answerListener);
    }

    public void doGetStatusAction(GameStatusMessageController parsedMessage) throws ControllerException {
        Answer gameStatusAnswer = parsedMessage.doAction(this);
        for(AnswerListener a: listeners){
            // we send the game status only to who required it
            if(a.getPlayerId() == gameStatusAnswer.getPlayerId()) a.sendAnswer(gameStatusAnswer);
        }
    }



}
