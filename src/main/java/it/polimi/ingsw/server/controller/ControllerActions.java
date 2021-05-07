package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.states.EndGameState;
import it.polimi.ingsw.server.controller.states.GamePlayState;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.controller.states.State;
import it.polimi.ingsw.server.model.game.Game;

/**
 * class that handles the actions of the players and calls the methods of the model
 * @param <T> the type of the game, it can be single player or multi player
 */
public abstract class ControllerActions<T extends Game<?>> {
    protected  T game;
    private final int gameId;
    private State gameState;
    private static final ControllerManager controllerManager = ControllerManager.getInstance();

    /**
     * method called to create a single player controller
     * @param game single player game
     * @param id gameId
     */
    public ControllerActions(T game, int id) {
        this.game = game;
        this.gameId = id;
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
    public synchronized void toPrepareGameState(){
        game.distributeLeader();
        this.gameState = new PrepareGameState();
    }

    public synchronized void toGamePlayState(){
        this.gameState = new GamePlayState();
    }

    public synchronized void toEndGameState(){
        this.gameState = new EndGameState();
    }

    public synchronized void doAction(ClientMessageController clientMessage){
        clientMessage.doAction(this);
    }
}
