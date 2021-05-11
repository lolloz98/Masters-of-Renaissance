package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;
import it.polimi.ingsw.server.controller.states.FlushResourcesState;

public class FlushProductionResMessageController extends PlayingMessageController{

    public FlushProductionResMessageController(FlushProductionResMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        return null;
    }

    @Override
    public boolean checkState(ControllerActions<?> controllerActions){
        return controllerActions.getGameState() instanceof FlushResourcesState;
    }
}
