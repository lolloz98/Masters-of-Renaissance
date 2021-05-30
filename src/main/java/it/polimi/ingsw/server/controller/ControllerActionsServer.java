package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.endgameanswer.DestroyedGameAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.Persist;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Turn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class that handles the actions of the players and calls the methods of the model
 *
 * @param <T> the type of the game, it can be single player or multi player
 */
public abstract class ControllerActionsServer<T extends Game<? extends Turn>> extends ControllerActionsBase<T> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsServer.class);

    private static final ControllerManager controllerManager = ControllerManager.getInstance();

    private final List<AnswerListener> listeners = Collections.synchronizedList(new ArrayList<>());

    /**
     * method called to create a controller
     *
     * @param game current game
     * @param id   gameId
     */
    public ControllerActionsServer(T game, int id, AnswerListener answerListener) {
        super(game, id);
        addAnswerListener(answerListener);
    }

    /**
     * persist the status of this game. (it saves the game on a file)
     */
    protected void persist(){
        try {
            logger.debug("Persisting status of game");
            Persist.getInstance().persist(game, gameId);
        } catch (IOException e) {
            logger.error("Exception while persisting: " + e);
        }
    }

    /**
     * delete the file related to this game
     */
    private void destroy(){
        logger.debug("destroying file related to gameId: " + gameId);
        Persist.getInstance().remove(gameId);
    }

    public synchronized void doAction(ClientMessageController clientMessage) throws ControllerException {
        Answer answer = clientMessage.doAction(this);
        persist();
        sendAnswer(answer);
    }

    /**
     * Do a pregame action: if the game is created send the entire game status to all players (each one will receive a slightly different status )
     *
     * @param clientMessage request
     * @param answerListener answerListener related to the player sending the request
     */
    public synchronized void doPreGameAction(PreGameCreationMessageController clientMessage, AnswerListener answerListener) throws ControllerException {
        Answer answer = clientMessage.doAction(this);
        answerListener.setIds(answer.getPlayerId(), answer.getGameId());
        addAnswerListener(answerListener);
        if (game != null) {
            persist();
            sendGameStatusToAll(answer.getGameId(), answer.getPlayerId());
        } else{
            sendAnswer(answer);
        }
    }

    /**
     * Do a discardLeader or removeLeader action.
     * @param parsedMessage request
     */
    public void doDiscardOrRemoveLeader(ClientMessageController parsedMessage) throws ControllerException {
        Answer answer = parsedMessage.doAction(this);
        Answer answer2;

        if(answer instanceof RemoveLeaderPrepAnswer){
            answer2 = AnswerFactory.createConcealedRemoveLeaderPrepAnswer((RemoveLeaderPrepAnswer) answer);
        }else if(answer instanceof DiscardLeaderAnswer){
            answer2 = AnswerFactory.createConcealedDiscardLeaderAnswer((DiscardLeaderAnswer) answer);
        }else{
            logger.error("The answer to a " + parsedMessage.getClass().getSimpleName() + " is neither DiscardLeaderAnswer nor RemoveLeaderPrepAnswer");
            throw new UnexpectedControllerException("Something unexpected happened while prepararing the answer to your request");
        }

        persist();

        for(AnswerListener answerListener: listeners){
            if(answerListener.getPlayerId() == answer.getPlayerId()) answerListener.sendAnswer(answer);
            else answerListener.sendAnswer(answer2);
        }
    }

    public synchronized void sendGameStatusToAll(int gameId, int playerIdLastRequest) throws UnexpectedControllerException {
        for (AnswerListener a : listeners) {
            a.sendAnswer(AnswerFactory.createGameStatusAnswer(gameId, playerIdLastRequest, a.getPlayerId(), game));
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

        destroy();
    }
}
