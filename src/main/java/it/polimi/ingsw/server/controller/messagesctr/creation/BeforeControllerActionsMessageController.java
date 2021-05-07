package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;

import java.io.Serializable;

/**
 * father of messages that precedes the creation of a controllerActions
 */
public abstract class BeforeControllerActionsMessageController implements Serializable {
    private static final long serialVersionUID = 201L;

    private final ClientMessage clientMessage;

    public BeforeControllerActionsMessageController(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public abstract Answer doAction() throws ControllerException;
}
