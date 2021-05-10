package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;

public class DiscardLeaderMessageController extends PlayingMessageController {


    public DiscardLeaderMessageController(DiscardLeaderMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        return null;
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return false;
    }
}
