package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.requests.ClientMessage;

/**
 * every request that can come only from the current player must inherit from this class
 */
public abstract class CurrentPlayerMessageController extends ClientMessageController{
    private static final long serialVersionUID = 206L;

    public CurrentPlayerMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }
}
