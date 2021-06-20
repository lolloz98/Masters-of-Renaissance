package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.RejoinMessage;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is useful only for parsing the message.
 */
public class RejoinMessageController extends ClientMessageController{
    private static final Logger logger = LogManager.getLogger(RejoinMessageController.class);

    public RejoinMessageController(RejoinMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws ControllerException {
        logger.error("This method should never be called (class only used for parsing purposes)");
        return null;
    }

    @Override
    protected boolean checkState(ControllerActionsBase<?> controllerActions) {
        logger.error("This method should never be called (class only used for parsing purposes)");
        return false;
    }
}
