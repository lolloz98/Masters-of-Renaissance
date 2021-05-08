package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;
import it.polimi.ingsw.server.controller.messagesctr.CurrentPlayerMessageController;
import it.polimi.ingsw.server.controller.states.GamePlayState;
import it.polimi.ingsw.server.controller.states.PrepareGameState;

public abstract class PlayingMessageController extends CurrentPlayerMessageController {

    public PlayingMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public abstract Answer doAction(ControllerActions<?> controllerActions) throws ControllerException, NotCurrentPlayerException;

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() instanceof GamePlayState;
    }
}
