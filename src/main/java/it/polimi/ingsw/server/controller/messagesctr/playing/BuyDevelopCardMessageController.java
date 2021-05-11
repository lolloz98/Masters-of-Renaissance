package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;

//todo
public class BuyDevelopCardMessageController extends PlayingMessageController{
    public BuyDevelopCardMessageController(BuyDevelopCardMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        // todo
        return null;
    }
}
