package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.GameStatusMessage;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;

public class GameStatusMessageController extends ClientMessageController {

    public GameStatusMessageController(GameStatusMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        return AnswerFactory.createGameStatusAnswer(controllerActions.getGameId(),
                getClientMessage().getPlayerId(), getClientMessage().getPlayerId(), controllerActions.getGame());
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        // I can always require the game status unless the game has not been initialized
        return controllerActions.getGame() != null;
    }
}
