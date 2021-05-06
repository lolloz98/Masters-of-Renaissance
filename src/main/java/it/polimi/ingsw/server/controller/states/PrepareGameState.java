package it.polimi.ingsw.server.controller.states;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.PreparationMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.states.State;

/**
 * representation of the preparation state of the game
 */
public class PrepareGameState implements State {
    @Override
    public void doAction(ClientMessage clientMessage, ControllerActions<?> controllerActions) {
        PreparationMessage message=(PreparationMessage) clientMessage;
    }
}
