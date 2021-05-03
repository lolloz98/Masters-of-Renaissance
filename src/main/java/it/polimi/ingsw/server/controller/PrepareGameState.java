package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.PreparationMessage;

/**
 * representation of the preparation state of the game
 */
public class PrepareGameState implements State{
    @Override
    public void doAction(ClientMessage clientMessage, ControllerActions controllerActions) {
        PreparationMessage message=(PreparationMessage) clientMessage;

    }
}
