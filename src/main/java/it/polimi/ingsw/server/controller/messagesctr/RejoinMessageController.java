package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.RejoinMessage;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;

/**
 * This class is useful only for parsing the message.
 */
public class RejoinMessageController extends ClientMessageController{
    public RejoinMessageController(RejoinMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws ControllerException {
        return null;
    }

    @Override
    protected boolean checkState(ControllerActionsBase<?> controllerActions) {
        return false;
    }
}
