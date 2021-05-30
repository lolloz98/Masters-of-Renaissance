package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Turn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class ControllerActionsBase<T extends Game<? extends Turn>> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsServer.class);

    protected T game;
    protected final int gameId;
    protected State gameState;

    public ControllerActionsBase(T game, int gameId) {
        this.game = game;
        this.gameId = gameId;
    }

    protected void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public synchronized T getGame() {
        return game;
    }

    public synchronized int getGameId() {
        return gameId;
    }

    public abstract int getConnectedPlayers();

    public abstract boolean checkToGamePlayState();

    public synchronized void toGamePlayState() {
        this.gameState = State.PLAY;
    }

    public synchronized void toEndGameState() {
        this.gameState = State.OVER;
    }

    public synchronized State getGameState() {
        return gameState;
    }

    public abstract void doAction(ClientMessageController clientMessage) throws ControllerException;

    public abstract void doPreGameAction(PreGameCreationMessageController clientMessage, AnswerListener answerListener) throws ControllerException;

    public abstract void doDiscardOrRemoveLeader(ClientMessageController parsedMessage) throws ControllerException;

    public abstract void sendGameStatusToAll(int gameId, int playerIdLastRequest) throws UnexpectedControllerException;

    public abstract void doGetStatusAction(GameStatusMessageController parsedMessage) throws ControllerException;

    public abstract void removeLeadersEffect() throws UnexpectedControllerException;

    public abstract void applyLeadersEffect() throws UnexpectedControllerException;

    /**
     * Creates the list of the winners
     * @throws UnexpectedControllerException if the game is not over
     */
    public abstract ArrayList<LocalPlayer> getWinners() throws UnexpectedControllerException;

    public abstract void destroyGame(String message, int playerId, boolean removePlayerIdListener) throws NoSuchControllerException;


}