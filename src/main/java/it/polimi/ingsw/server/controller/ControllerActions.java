package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.GameManager;

/**
 * class that handle the actions on the player and calls the methods of the model
 * @param <T> the instance of the game, it can be single player or multi player
 */
public class ControllerActions<T extends Game<?>> {
    protected  T game;
    private int gameId;
    private State gameState;
    private final GameManager gameManager;

    /**
     * method called to create a single player controller
     * @param game single player game
     * @param id gameId
     */
    public ControllerActions(T game, int id, GameManager gameManager) {
        this.game = game;
        this.gameId=id;
        this.gameManager=gameManager;
        gameState=new PrepareGameState();
    }

    /**
     * method called to create a multi player controller
     */
    public ControllerActions(GameManager gameManager) {
        this.gameManager=gameManager;
        gameState=new WaitingState();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public synchronized T getGame() {
        return game;
    }

    /**
     * method that changes the state of the game: from waitingState to prepareGameState
     */
    public void prepareGame(){
        this.gameState=new PrepareGameState();
    }




}
