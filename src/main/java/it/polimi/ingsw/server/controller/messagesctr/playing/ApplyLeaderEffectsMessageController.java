package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;

public class ApplyLeaderEffectsMessageController extends PlayingMessageController {
    public ApplyLeaderEffectsMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        // todo
        return null;
    }
}
