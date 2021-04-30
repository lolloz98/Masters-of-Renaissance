package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exception.GameAlreadyStartedException;
import it.polimi.ingsw.model.exception.NoSuchReservedIdException;
import it.polimi.ingsw.model.game.GameManager;
import it.polimi.ingsw.requests.ClientMessage;
import it.polimi.ingsw.requests.JoinGameMessage;

public class WaitingState implements State{

    /**
     * method that adds a player in waiting room
     * @param clientMessage sent by the player that wants to join in a specified game
     * @param controllerActions
     */
    @Override
    public void doAction(ClientMessage clientMessage, ControllerActions controllerActions) {
        GameManager gameManager=controllerActions.getGameManager();
        JoinGameMessage message=(JoinGameMessage) clientMessage;
        try {
            gameManager.addPlayerToGame(message.getId(), message.getUserName());
        } catch (GameAlreadyStartedException e) {
            //todo
            e.printStackTrace();
        } catch (NoSuchReservedIdException e) {
            //todo
            e.printStackTrace();
        }
        /*
        if(gameManager.containsGame(message.getId()))//if the game is ready to begin
            controllerActions.prepareGame();

*/

    }
}
