package it.polimi.ingsw.controller;

import it.polimi.ingsw.requests.ClientMessage;

/**
 * implementation of state pattern. it represents the state in which the game could be
 */
public interface State {
    public void doAction(ClientMessage clientMessage, ControllerActions controllerActions);
}
