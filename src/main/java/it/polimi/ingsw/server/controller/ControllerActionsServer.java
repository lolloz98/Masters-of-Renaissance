package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.answers.endgameanswer.DestroyedGameAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.answers.preparationanswer.RemoveLeaderPrepAnswer;
import it.polimi.ingsw.messages.requests.RejoinMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.AlreadyInAGameControllerException;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.Persist;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.game.Turn;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public abstract class ControllerActionsServer<T extends Game<? extends Turn>> extends ControllerActionsBase<T> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsServer.class);

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
     * useful if we need to rejoin the game later
     */
    public ControllerActionsServer(T game, int id){
        super(game, id);
    }

    @Override
    public synchronized int getConnectedPlayers() {
        return listeners.size();
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
    public synchronized void doDiscardOrRemoveLeader(ClientMessageController parsedMessage) throws ControllerException {
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
        ControllerManager.getInstance().removeGame(getGameId());

        listeners.removeIf(x -> removePlayerIdListener && x.getPlayerId() == playerId);

        for(AnswerListener answerListener: listeners){
            // before sending the answer remove the answer listener
            answerListener.setIds(-1, -1);
        }

        sendAnswer(destroyAnswer);

        listeners.clear();

        destroy();
    }

    /**
     * after checking necessary conditions on the controller and the game,
     * it sends the gameStatus to all the clients connected to the game
     * and it binds who is requiring to this game.
     *
     * @param answerListener answer listener of client who wants to rejoin the game
     * @param rejoinMessage message from client who wants to rejoin the game
     */
    public synchronized void rejoin(AnswerListener answerListener, RejoinMessage rejoinMessage) throws AlreadyInAGameControllerException, UnexpectedControllerException {
        if(answerListener.getPlayerId() != -1 || answerListener.getGameId() != -1) throw new AlreadyInAGameControllerException();
        if(listeners.contains(answerListener)) throw new UnexpectedControllerException("You are already part of this game, you cannot rejoin");
        for(AnswerListener a: listeners){
            if(a.getPlayerId() == rejoinMessage.getPlayerId()) throw new UnexpectedControllerException("The player with ID: " + rejoinMessage.getPlayerId() + " has already rejoined the game");
        }

        boolean exist = false;
        int numberOfPlayers = 0;

        if(game instanceof SinglePlayer && ((SinglePlayer) game).getPlayer().getPlayerId() == rejoinMessage.getPlayerId()) {
            exist = true;
            numberOfPlayers = 1;
        }
        else{
            for(Player p: ((MultiPlayer) game).getPlayers()){
                if (p.getPlayerId() == rejoinMessage.getPlayerId()) {
                    exist = true;
                    break;
                }
            }
            numberOfPlayers = ((MultiPlayer) game).getPlayers().size();
        }
        if(exist){
            if(listeners.size() == 0){
                if(!ControllerManager.getInstance().removeFromToBeRejoined(rejoinMessage.getGameId())){
                    logger.error("Unexpected: after checks it seems that we cannot rejoin the game anyway");
                    throw new UnexpectedControllerException("No game in which to be rejoined to");
                }
            }
            answerListener.setIds(rejoinMessage.getPlayerId(), rejoinMessage.getGameId());
            listeners.add(answerListener);
        }else throw new UnexpectedControllerException("No player with such ID found in the game specified");

        boolean waiting = listeners.size() != numberOfPlayers;

        if(numberOfPlayers < listeners.size()) logger.error("The game might be compromised: more listeners than players");
        logger.info("number of listeners: " + listeners.size() + " number of players: " + numberOfPlayers);

        for (AnswerListener a : listeners) {
            GameStatusAnswer gameStatusAnswer = AnswerFactory.createGameStatusAnswer(gameId, rejoinMessage.getPlayerId(), a.getPlayerId(), game);
            if(waiting) gameStatusAnswer.getGame().setState(LocalGameState.WAIT_FOR_REJOIN);
            a.sendAnswer(gameStatusAnswer);
        }
    }
}
