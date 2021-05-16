package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;

/**
 * it represents the state in which the game is
 */
public enum State {
    WAITING_FOR_PLAYERS,
    PREPARATION,
    PLAY,
    OVER
}
