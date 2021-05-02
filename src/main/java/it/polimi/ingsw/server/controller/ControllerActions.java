package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.game.Game;

import java.util.TreeMap;

/**
 * class that handle the actions on the player and calls the methods of the model
 * @param <T> the instance of the game, it can be single player or multi player
 */
public abstract class ControllerActions<T extends Game<?>> {
    protected  T game;
    private int gameId;
    private State gameState;
    private static final ControllerManager controllerManager = ControllerManager.getInstance();

    /**
     * method called to create a single player controller
     * @param game single player game
     * @param id gameId
     */
    public ControllerActions(T game, int id) {
        this.game = game;
        this.gameId=id;
        gameState=new PrepareGameState();
    }

    public synchronized T getGame() {
        return game;
    }

    /**
     * method that changes the state of the game: from waitingState to prepareGameState
     * and prepares the game to be played
     */
    public synchronized void prepareGame(){
        this.gameState=new PrepareGameState();
        game.distributeLeader();
        distributeBeginningRes();
    }

    protected abstract void distributeBeginningRes();




}
