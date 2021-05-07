package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;

import java.io.Serializable;

public abstract class ClientMessageController implements Serializable {
    private static final long serialVersionUID = 200L;

    private final ClientMessage clientMessage;

    public ClientMessageController(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public abstract void doAction(ControllerActions<?> controllerActions);

    protected abstract void checkState(ControllerActions<?> controllerActions);
}
